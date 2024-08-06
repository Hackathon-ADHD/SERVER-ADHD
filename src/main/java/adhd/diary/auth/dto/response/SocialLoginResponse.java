package adhd.diary.auth.dto.response;

public class SocialLoginResponse {

    private String email;
    private String accessToken;
    private String refreshToken;
    private boolean isNewMember;

    public SocialLoginResponse(String email,
                               String accessToken,
                               String refreshToken,
                               boolean isNewMember) {
        this.email = email;
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.isNewMember = isNewMember;
    }

    public String getEmail() {
        return email;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public boolean isNewMember() {
        return isNewMember;
    }
}