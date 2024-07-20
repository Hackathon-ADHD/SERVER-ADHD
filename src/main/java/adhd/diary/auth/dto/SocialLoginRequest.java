package adhd.diary.auth.dto;

import adhd.diary.member.domain.SocialProvider;

public class SocialLoginRequest {

    private String email;
    private String socialId;
    private SocialProvider socialProvider;

    public SocialLoginRequest(String email,
                              String socialId,
                              SocialProvider socialProvider) {
        this.email = email;
        this.socialId = socialId;
        this.socialProvider = socialProvider;
    }

    public String getEmail() {
        return email;
    }

    public String getSocialId() {
        return socialId;
    }

    public SocialProvider getSocialProvider() {
        return socialProvider;
    }
}
