package adhd.diary.auth.service;

import adhd.diary.auth.CustomOAuth2User;
import adhd.diary.auth.OAuthAttributes;
import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.MemberRepository;
import adhd.diary.member.domain.SocialProvider;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import static adhd.diary.member.domain.SocialProvider.GOOGLE;

@Service
@Transactional
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final MemberRepository memberRepository;

    private static final String NAVER = "naver";
    private static final String KAKAO = "kakao";
    private static final String GOOGLE = "google";

    public CustomOAuth2UserService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> delegate = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = delegate.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();

        String registrationId = userRequest.getClientRegistration().getRegistrationId();
        SocialProvider socialProvider = getSocialProvider(registrationId);
        String userNameAttributeName = userRequest.getClientRegistration()
                .getProviderDetails()
                .getUserInfoEndpoint()
                .getUserNameAttributeName();

        OAuthAttributes extractAttributes = OAuthAttributes.of(socialProvider, userNameAttributeName, attributes);

        Member member = getMember(extractAttributes, socialProvider);

        return new CustomOAuth2User(
                Collections.singleton(new SimpleGrantedAuthority(member.getRole().getKey())),
                attributes,
                extractAttributes.getNameAttributeKey(),
                member.getEmail(),
                member.getRole(),
                member.getSocialId()
        );
    }

    private SocialProvider getSocialProvider(String registrationId) {
        if(NAVER.equals(registrationId)) {
            return SocialProvider.NAVER;
        }
        else if(KAKAO.equals(registrationId)) {
            return SocialProvider.KAKAO;
        }
        else if(GOOGLE.equals(registrationId)) {
            return SocialProvider.GOOGLE;
        }
        return null;
    }

    private Member getMember(OAuthAttributes attributes, SocialProvider socialProvider) {

        Member findMember = memberRepository.findBySocialProviderAndSocialId(socialProvider, attributes.getoAuth2UserInfo().getId()).orElse(null);

        if(findMember == null) {
            return saveMember(attributes, socialProvider);
        }

        return findMember;
    }

    private Member saveMember(OAuthAttributes attributes, SocialProvider socialProvider) {
        Member createdMember = attributes.toEntity(socialProvider, attributes.getoAuth2UserInfo());
        return memberRepository.save(createdMember);
    }
}
