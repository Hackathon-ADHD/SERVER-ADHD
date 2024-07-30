package adhd.diary.auth.service;

import adhd.diary.auth.dto.request.SocialLoginRequest;
import adhd.diary.auth.dto.response.MemberLoginResponse;
import adhd.diary.auth.dto.response.SocialLoginResponse;
import adhd.diary.auth.exception.login.LoginNotFoundException;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.auth.userinfo.GoogleOAuth2UserInfo;
import adhd.diary.auth.userinfo.OAuth2UserInfo;
import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.MemberRepository;
import adhd.diary.member.domain.Role;
import adhd.diary.member.domain.SocialProvider;
import adhd.diary.response.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class GoogleService {

    @Value("${spring.security.oauth2.client.registration.google.client-id}")
    private String GOOGLE_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.google.redirect-uri}")
    private String GOOGLE_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.google.client-secret}")
    private String GOOGLE_CLIENT_SECRET;
    private static String GOOGLE_TOKEN_URI = "https://oauth2.googleapis.com/token";
    private static String GOOGLE_TOKEN_INFO= "https://www.googleapis.com/oauth2/v2/userinfo";

    @Autowired
    private RestTemplate restTemplate;

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public GoogleService(MemberRepository memberRepository,
                         JwtService jwtService,
                         ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    public String getGoogleAccessToken(String code) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("code", code);
        params.add("client_id", GOOGLE_CLIENT_ID);
        params.add("client_secret", GOOGLE_CLIENT_SECRET);
        params.add("redirect_uri", GOOGLE_REDIRECT_URI);
        params.add("grant_type", "authorization_code");

        HttpEntity<MultiValueMap<String, String>> googleTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> response= restTemplate.exchange(
                GOOGLE_TOKEN_URI,
                HttpMethod.POST,
                googleTokenRequest,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("access_token").asText();
            } catch (JsonProcessingException e) {
                throw new LoginNotFoundException(ResponseCode.GOOGLE_TOKEN_RETRIEVAL_FAILED);
            }
        } else {
            throw new LoginNotFoundException(ResponseCode.GOOGLE_TOKEN_RETRIEVAL_FAILED);
        }
    }

    public OAuth2UserInfo getUserGoogleInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String,String>> googleRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response= restTemplate.exchange(
                GOOGLE_TOKEN_INFO,
                HttpMethod.GET,
                googleRequest,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                Map<String, Object> attributes = objectMapper.readValue(response.getBody(), HashMap.class);
                return new GoogleOAuth2UserInfo(attributes);
            } catch (JsonProcessingException e) {
                throw new LoginNotFoundException(ResponseCode.GOOGLE_USER_INFO_RETRIEVAL_FAILED);
            }
        } else {
            throw new LoginNotFoundException(ResponseCode.GOOGLE_USER_INFO_RETRIEVAL_FAILED);
        }
    }

    @Transactional
    public SocialLoginResponse googleLogin(String accessToken) {
        OAuth2UserInfo userInfo = getUserGoogleInfo(accessToken);
        MemberLoginResponse memberResponse = findBySocialId(userInfo.getId());

        if(memberResponse == null) {
            signUp(new SocialLoginRequest(userInfo.getEmail(), userInfo.getId()));
            memberResponse = findBySocialId(userInfo.getId());
        }

        String jwtAccessToken = jwtService.createAccessToken(memberResponse.getEmail());
        String jwtRefreshToken = jwtService.createRefreshToken(memberResponse.getEmail());

        jwtService.updateRefreshToken(memberResponse.getEmail(), jwtRefreshToken);

        return new SocialLoginResponse(jwtAccessToken, jwtRefreshToken, memberResponse.getEmail());
    }

    @Transactional
    public MemberLoginResponse findBySocialId(String socialId) {
        Member member = memberRepository.findBySocialId(socialId).orElse(null);
        return member != null ? new MemberLoginResponse(member.getEmail(), member.getSocialId()) : null;
    }

    @Transactional
    public Long signUp(SocialLoginRequest socialLoginRequest) {
        Member member = Member.builder()
                .socialProvider(SocialProvider.GOOGLE)
                .role(Role.USER)
                .email(socialLoginRequest.getEmail())
                .socialId(socialLoginRequest.getSocialId())
                .build();
        return memberRepository.save(member).getId();
    }
}