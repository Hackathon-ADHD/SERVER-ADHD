package adhd.diary.auth.dto.response;

public class SocialLoginResponse {

    private String email;
    private String accessToken;
    private String refreshToken;

    public SocialLoginResponse(String accessToken,
                               String refreshToken,
                               String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.email = email;
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
}