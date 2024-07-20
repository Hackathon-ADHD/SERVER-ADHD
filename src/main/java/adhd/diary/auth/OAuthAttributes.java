package adhd.diary.auth;

import adhd.diary.auth.userinfo.GoogleOAuth2UserInfo;
import adhd.diary.auth.userinfo.KakaoOAuth2UserInfo;
import adhd.diary.auth.userinfo.NaverOAuth2UserInfo;
import adhd.diary.auth.userinfo.OAuth2UserInfo;
import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.Role;
import adhd.diary.member.domain.SocialProvider;

import java.util.Map;

public class OAuthAttributes {

    private String nameAttributeKey;
    private OAuth2UserInfo oAuth2UserInfo;

    public OAuthAttributes(String nameAttributeKey, OAuth2UserInfo oAuth2UserInfo) {
        this.nameAttributeKey = nameAttributeKey;
        this.oAuth2UserInfo = oAuth2UserInfo;
    }

    public static class Builder {
        private String nameAttributeKey;
        private OAuth2UserInfo oAuth2UserInfo;

        public Builder nameAttributeKey(String nameAttributeKey) {
            this.nameAttributeKey = nameAttributeKey;
            return this;
        }

        public Builder oAuth2UserInfo(OAuth2UserInfo oAuth2UserInfo) {
            this.oAuth2UserInfo = oAuth2UserInfo;
            return this;
        }

        public OAuthAttributes build() {
            return new OAuthAttributes(nameAttributeKey, oAuth2UserInfo);
        }
    }

    public static Builder builder() {
        return new Builder();
    }

    public String getNameAttributeKey() {
        return nameAttributeKey;
    }

    public OAuth2UserInfo getoAuth2UserInfo() {
        return oAuth2UserInfo;
    }

    @Override
    public String toString() {
        return "OAuthAttributes{" +
                "nameAttributeKey='" + nameAttributeKey + '\'' +
                ", oAuth2UserInfo=" + oAuth2UserInfo +
                '}';
    }

    public static OAuthAttributes of(SocialProvider socialProvider,
                                     String userNameAttributeName,
                                     Map<String, Object> attributes) {

        if (socialProvider == SocialProvider.KAKAO) {
            return ofKakao(userNameAttributeName, attributes);
        }
        else if (socialProvider == SocialProvider.GOOGLE) {
            return ofGoogle(userNameAttributeName, attributes);
        }
        else if (socialProvider == SocialProvider.NAVER) {
            return ofNaver(userNameAttributeName, attributes);
        }
        return null;
    }

    private static OAuthAttributes ofKakao(String userNameAttributesName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributesName)
                .oAuth2UserInfo(new KakaoOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofGoogle(String userNameAttributesName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributesName)
                .oAuth2UserInfo(new GoogleOAuth2UserInfo(attributes))
                .build();
    }

    private static OAuthAttributes ofNaver(String userNameAttributesName, Map<String, Object> attributes) {
        return OAuthAttributes.builder()
                .nameAttributeKey(userNameAttributesName)
                .oAuth2UserInfo(new NaverOAuth2UserInfo(attributes))
                .build();
    }

    public Member toEntity(SocialProvider socialProvider, OAuth2UserInfo oAuth2UserInfo) {
        return Member.builder()
                .email(oAuth2UserInfo.getEmail())
                .socialId(oAuth2UserInfo.getId())
                .role(Role.USER)
                .socialProvider(socialProvider)
                .build();
    }
}
