package adhd.diary.auth.service;

import adhd.diary.auth.dto.request.SocialLoginRequest;
import adhd.diary.auth.dto.response.MemberLoginResponse;
import adhd.diary.auth.dto.response.SocialLoginResponse;
import adhd.diary.auth.exception.NaverMemberRequestFailedException;
import adhd.diary.auth.exception.NaverTokenRequestFailedException;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.auth.userinfo.NaverOAuth2UserInfo;
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
public class NaverService {

    @Value("${spring.security.oauth2.client.registration.naver.client-id}")
    private String NAVER_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.naver.redirect-uri}")
    private String NAVER_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.naver.client-secret}")
    private String NAVER_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.provider.naver.token-uri}")
    private String NAVER_TOKEN_URI;

    @Value(("${spring.security.oauth2.client.provider.naver.user-info-uri}"))
    private String NAVER_USER_INFO_URI;

    @Autowired
    private RestTemplate restTemplate;

    private final MemberRepository memberRepository;
    private final JwtService jwtService;
    private final ObjectMapper objectMapper;

    public NaverService(MemberRepository memberRepository,
                        JwtService jwtService,
                        ObjectMapper objectMapper) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
        this.objectMapper = objectMapper;
    }

    public String getNaverAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", NAVER_CLIENT_ID);
        params.add("redirect_uri", NAVER_REDIRECT_URI);
        params.add("code", code);
        params.add("client_secret", NAVER_CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> naverTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> response = restTemplate.exchange(
                NAVER_TOKEN_URI,
                HttpMethod.POST,
                naverTokenRequest,
                String.class
        );
        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                JsonNode jsonNode = objectMapper.readTree(response.getBody());
                return jsonNode.get("access_token").asText();
            } catch (JsonProcessingException e) {
                throw new NaverTokenRequestFailedException(ResponseCode.NAVER_TOKEN_RETRIEVAL_FAILED);
            }
        } else {
            throw new NaverTokenRequestFailedException(ResponseCode.NAVER_TOKEN_RETRIEVAL_FAILED);
        }
    }

    public OAuth2UserInfo getUserNaverInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> naverUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> response = restTemplate.exchange(
                NAVER_USER_INFO_URI,
                HttpMethod.GET,
                naverUserInfoRequest,
                String.class
        );

        if (response.getStatusCode() == HttpStatus.OK && response.getBody() != null) {
            try {
                Map<String, Object> attributes = objectMapper.readValue(response.getBody(), HashMap.class);
                return new NaverOAuth2UserInfo(attributes);
            } catch (JsonProcessingException e) {
                throw new NaverMemberRequestFailedException(ResponseCode.NAVER_USER_INFO_RETRIEVAL_FAILED);
            }
        } else {
            throw new NaverMemberRequestFailedException(ResponseCode.NAVER_USER_INFO_RETRIEVAL_FAILED);
        }
    }

    @Transactional
    public SocialLoginResponse naverLogin(String accessToken) {
        OAuth2UserInfo userInfo = getUserNaverInfo(accessToken);
        MemberLoginResponse memberResponse = findBySocialId(userInfo.getId());

        boolean isNewMember = false;

        if(memberResponse == null) {
            signUp(new SocialLoginRequest(userInfo.getEmail(), userInfo.getId()));
            memberResponse = findBySocialId(userInfo.getId());
            isNewMember = true;
        }

        String jwtAccessToken = jwtService.createAccessToken(memberResponse.getEmail());
        String jwtRefreshToken = jwtService.createRefreshToken(memberResponse.getEmail());

        jwtService.updateRefreshToken(memberResponse.getEmail(), jwtRefreshToken);

        return new SocialLoginResponse(jwtAccessToken, jwtRefreshToken, memberResponse.getEmail(), isNewMember);
    }

    @Transactional
    public MemberLoginResponse findBySocialId(String socialId) {
        Member member = memberRepository.findBySocialId(socialId).orElse(null);
        return member != null ? new MemberLoginResponse(member.getEmail(), member.getSocialId()) : null;
    }

    @Transactional
    public Long signUp(SocialLoginRequest socialLoginRequest) {
        Member member = Member.builder()
                .socialProvider(SocialProvider.NAVER)
                .role(Role.USER)
                .email(socialLoginRequest.getEmail())
                .socialId(socialLoginRequest.getSocialId())
                .build();
        return memberRepository.save(member).getId();
    }
}