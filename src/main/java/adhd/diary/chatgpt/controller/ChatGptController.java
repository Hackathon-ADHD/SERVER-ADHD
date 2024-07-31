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

    @Value("${chatgpt.emotion-analyze}")
    private String analyzeSentence;

    @Value("${chatgpt.recommend-song}")
    private String recommendSongSentence;

    public ChatGptController(ChatGptService chatGptService) {
        this.chatGptService = chatGptService;
    }

    @PostMapping("/chatgpt/diary-analyze")
    public CompletableFuture<ResponseEntity<String>> getAnalyzedDiary(@RequestParam String diaryContents) {
        return chatGptService.analyzeDiary(List.of(new ChatMessage(diaryContents + analyzeSentence)))
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage()));
    }

    @PostMapping("/chatgpt/recommend-song")
    public CompletableFuture<ResponseEntity<String>> getRecommendSong(@RequestParam String diaryContents) {
        return chatGptService.recommendSong(List.of(new ChatMessage(diaryContents + recommendSongSentence)))
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage()));
    }
}
