package adhd.diary.chatgpt.service;

import adhd.diary.chatgpt.dto.ChatCompletionRequest;
import adhd.diary.chatgpt.dto.ChatMessage;
import adhd.diary.chatgpt.util.ChatGptUtil;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ChatGptService {

    @Value("${chatgpt.model}")
    private String model;

    @Value("${openai.url.prompt}")
    private String API_URL;

    @Value("${chatgpt.max-tokens}")
    private Integer maxTokens;

    private final ChatGptUtil chatGptUtil;

    public ChatGptService(ChatGptUtil chatGptUtil) {
        this.chatGptUtil = chatGptUtil;
    }

    public String analyzeDiary(List<ChatMessage> messages) {
        return chatGptUtil.createChatCompletion(toChatCompletionRequest(messages), API_URL);
    }

    public String recommendSong(List<ChatMessage> messages) {
        return chatGptUtil.createChatCompletion(toChatCompletionRequest(messages), API_URL);
    }

    public ChatCompletionRequest toChatCompletionRequest(List<ChatMessage> messages) {
        return new ChatCompletionRequest(model, messages, maxTokens);
    }
}
