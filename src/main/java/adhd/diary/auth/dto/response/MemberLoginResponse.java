package adhd.diary.auth.dto.response;

public class MemberLoginResponse {
    private String email;

    public MemberLoginResponse(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}
