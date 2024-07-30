package adhd.diary.auth.dto.request;

public class SocialLoginRequest {

    private String email;
    private String socialId;

    public SocialLoginRequest(String email, String socialId) {
        this.email = email;
        this.socialId = socialId;
    }

    public String getEmail() {
        return email;
    }

    public String getSocialId() {
        return socialId;
    }
}
