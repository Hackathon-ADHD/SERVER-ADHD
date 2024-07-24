package adhd.diary.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ChatCompletionChoice {

    private Integer index;

    @JsonAlias("delta")
    private ChatMessage message;

    @JsonProperty("finish_reason")
    private String finishReason;

    public Integer getIndex() {
        return index;
    }

    public ChatMessage getMessage() {
        return message;
    }

    public String getFinishReason() {
        return finishReason;
    }
}
