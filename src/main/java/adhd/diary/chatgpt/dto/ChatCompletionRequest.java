package adhd.diary.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;

public class ChatCompletionRequest {

    @Value("${chatgpt.model}")
    private String model;

    private List<ChatMessage> messages;

    @JsonProperty("max_tokens")
    @Value("${chatgpt.max-tokens}")
    private Integer maxTokens;

    public ChatCompletionRequest(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }
}
