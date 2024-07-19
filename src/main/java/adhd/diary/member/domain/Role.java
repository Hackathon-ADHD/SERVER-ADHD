package adhd.diary.member.domain;

public enum Role {

    USER("ROLE_USER", "회원"),
    ADMIN("ROLE_ADMIN", "관리자");

    private String key;
    private String title;

    Role(String key, String title) {
        this.key = key;
        this.title = title;
    }

    public String getKey() {
        return key;
    }

    public String getTitle() {
        return title;
    }


}
