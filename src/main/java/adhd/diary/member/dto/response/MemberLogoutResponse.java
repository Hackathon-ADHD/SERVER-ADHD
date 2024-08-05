package adhd.diary.member.dto.response;

import adhd.diary.member.domain.Role;
import adhd.diary.member.domain.SocialProvider;

import java.time.LocalDate;

public class MemberLogoutResponse {
    private String email;
    private SocialProvider socialProvider;
    private String socialId;
    private String refreshToken;

    public MemberLogoutResponse(String email,
                                SocialProvider socialProvider,
                                String socialId,
                                String refreshToken) {
        this.email = email;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
        this.refreshToken = refreshToken;
    }

    public String getEmail() {
        return email;
    }

    public SocialProvider getSocialProvider() {
        return socialProvider;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
