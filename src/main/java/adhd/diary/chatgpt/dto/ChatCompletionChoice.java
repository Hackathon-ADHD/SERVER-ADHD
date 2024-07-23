package adhd.diary.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatCompletionChoice {

    Integer index;

    @JsonAlias("delta")
    ChatMessage message;

    @JsonProperty("finish_reason")
    String finishReason;
}
