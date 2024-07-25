package adhd.diary.member.dto.request;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class UpdateNicknameRequest {

    private String nickname;

    public UpdateNicknameRequest() {
    }

    @JsonCreator
    public UpdateNicknameRequest(@JsonProperty("nickname") String nickname) {
        this.nickname = nickname;
    }

    public String getNickname() {
        return nickname;
    }
}
