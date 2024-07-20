package adhd.diary.member.dto;

import java.time.LocalDate;

public class CompleteRegistrationRequest {

    private String nickname;
    private LocalDate birthDay;

    public String getNickname() {
        return nickname;
    }

    public LocalDate getBirthDay() {
        return birthDay;
    }
}
