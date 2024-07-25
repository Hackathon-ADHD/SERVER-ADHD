package adhd.diary.member.dto.response;

import adhd.diary.member.domain.Role;
import adhd.diary.member.domain.SocialProvider;

import java.time.LocalDate;

public class MemberSignUpResponse {

    private String email;
    private Role role;
    private SocialProvider socialProvider;
    private String socialId;
    private String nickname;
    private LocalDate birthDay;

    public MemberSignUpResponse(String email,
                                Role role,
                                SocialProvider socialProvider,
                                String socialId,
                                String nickname,
                                LocalDate birthDay) {
        this.email = email;
        this.role = role;
        this.socialProvider = socialProvider;
        this.socialId = socialId;
        this.nickname = nickname;
        this.birthDay = birthDay;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public SocialProvider getSocialProvider() {
        return socialProvider;
    }

    public String getSocialId() {
        return socialId;
    }

    public String getNickname() {
        return nickname;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }
}
