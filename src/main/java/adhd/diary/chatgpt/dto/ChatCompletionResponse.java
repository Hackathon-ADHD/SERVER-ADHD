package adhd.diary.chatgpt.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public record ChatCompletionResponse(String id,
                                     String object,
                                     long created,
                                     String model,
                                     List<ChatCompletionChoice> choices,
                                     Usage usage) {
}
