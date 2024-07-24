package adhd.diary.auth.dto.request;

public class MemberKakaoSignupRequest {
    private String email;
    private String socialId;

    public MemberKakaoSignupRequest(String email, String socialId) {
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
