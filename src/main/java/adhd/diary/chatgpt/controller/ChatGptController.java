package adhd.diary.chatgpt.controller;

import adhd.diary.chatgpt.dto.ChatMessage;
import adhd.diary.chatgpt.service.ChatGptService;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class ChatGptController {

    private final ChatGptService chatGptService;

    @Value("${chatgpt.analyze}")
    private String analyzeSentence;

    public ChatGptController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    @PostMapping("/chatgpt/diary-analyze")
    public CompletableFuture<ResponseEntity<String>> createChatCompletion(@RequestParam String diaryContents) {
        return chatGptService.analyzeDiaryContents(List.of(new ChatMessage(diaryContents + analyzeSentence)))
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage()));
    }
}
