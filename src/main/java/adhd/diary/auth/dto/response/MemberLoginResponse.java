package adhd.diary.auth.dto.response;

public class MemberLoginResponse {
    private String email;
    private String socialId;

    public MemberLoginResponse(String email, String socialId) {
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
