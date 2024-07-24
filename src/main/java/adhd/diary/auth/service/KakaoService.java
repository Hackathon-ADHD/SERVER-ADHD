package adhd.diary.auth.service;

import adhd.diary.auth.dto.request.MemberKakaoSignupRequest;
import adhd.diary.auth.dto.response.KakaoTokenResponse;
import adhd.diary.auth.dto.response.MemberKakaoLoginResponse;
import adhd.diary.auth.dto.response.MemberLoginResponse;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.auth.userinfo.KakaoOAuth2UserInfo;
import adhd.diary.auth.userinfo.OAuth2UserInfo;
import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.MemberRepository;
import adhd.diary.member.domain.Role;
import adhd.diary.member.domain.SocialProvider;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.HashMap;
import java.util.Map;

@Service
public class KakaoService {

    @Value("${spring.security.oauth2.client.registration.kakao.client-id}")
    private String KAKAO_CLIENT_ID;

    @Value("${spring.security.oauth2.client.registration.kakao.redirect-uri}")
    private String KAKAO_REDIRECT_URI;

    @Value("${spring.security.oauth2.client.registration.kakao.client-secret}")
    private String KAKAO_CLIENT_SECRET;

    @Value("${spring.security.oauth2.client.provider.kakao.token-uri}")
    private String KAKAO_TOKEN_URI;

    @Value(("${spring.security.oauth2.client.provider.kakao.user-info-uri}"))
    private String KAKAO_USER_INFO_URI;


    @Autowired
    private RestTemplate restTemplate;

    private final MemberRepository memberRepository;
    private final JwtService jwtService;

    public KakaoService(MemberRepository memberRepository, JwtService jwtService) {
        this.memberRepository = memberRepository;
        this.jwtService = jwtService;
    }

    public String getKakaoAccessToken(String code) throws JsonProcessingException {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", KAKAO_CLIENT_ID);
        params.add("redirect_uri", KAKAO_REDIRECT_URI);
        params.add("code", code);
        params.add("client_secret", KAKAO_CLIENT_SECRET);

        HttpEntity<MultiValueMap<String, String>> kakaoTokenRequest = new HttpEntity<>(params, headers);
        ResponseEntity<String> accessTokenResponse = restTemplate.exchange(
            KAKAO_TOKEN_URI,
            HttpMethod.POST,
            kakaoTokenRequest,
            String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();

        try {
            KakaoTokenResponse kakaoTokenResponse = objectMapper.readValue(accessTokenResponse.getBody(), KakaoTokenResponse.class);
            return kakaoTokenResponse.getAccess_token();
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("카카오 액세스 토큰을 가져오는데 실패했습니다.", e);
        }
    }

    public OAuth2UserInfo getUserKakaoInfo(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + accessToken);
        headers.add("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        HttpEntity<MultiValueMap<String, String>> kakaoUserInfoRequest = new HttpEntity<>(headers);
        ResponseEntity<String> kakaoUserInfoResponse = restTemplate.exchange(
                KAKAO_USER_INFO_URI,
                HttpMethod.GET,
                kakaoUserInfoRequest,
                String.class
        );

        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new JavaTimeModule());
        objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);

        try {
            Map<String, Object> attributes = objectMapper.readValue(kakaoUserInfoResponse.getBody(), HashMap.class);
            return new KakaoOAuth2UserInfo(attributes);
        } catch (JsonProcessingException e) {
            throw new IllegalArgumentException("카카오 사용자 정보를 가져오는데 실패했습니다.", e);
        }
    }

    @Transactional
    public MemberKakaoLoginResponse kakaoLogin(String accessToken) {
        OAuth2UserInfo userInfo = getUserKakaoInfo(accessToken);
        MemberLoginResponse memberResponse = findBySocialId(userInfo.getId());

        if(memberResponse == null) {
            signUp(new MemberKakaoSignupRequest(userInfo.getEmail(), userInfo.getId()));
            memberResponse = findBySocialId(userInfo.getId());
        }

        String jwtAccessToken = jwtService.createAccessToken(memberResponse.getEmail());
        String jwtRefreshToken = jwtService.createRefreshToken(memberResponse.getEmail());

        jwtService.updateRefreshToken(memberResponse.getEmail(), jwtRefreshToken);

        return new MemberKakaoLoginResponse(jwtAccessToken, jwtRefreshToken, memberResponse.getEmail());
    }

    @Transactional
    public MemberLoginResponse findBySocialId(String socialId) {
        Member member = memberRepository.findBySocialId(socialId).orElse(null);
        return member != null ? new MemberLoginResponse(member.getEmail()) : null;
    }

    @Transactional
    public Long signUp(MemberKakaoSignupRequest memberKakaoSignupRequest) {
        Member member = Member.builder()
                .socialProvider(SocialProvider.KAKAO)
                .role(Role.USER)
                .email(memberKakaoSignupRequest.getEmail())
                .socialId(memberKakaoSignupRequest.getSocialId())
                .build();
        return memberRepository.save(member).getId();
    }
}
