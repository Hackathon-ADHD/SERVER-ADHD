package adhd.diary.chatgpt.controller;

import adhd.diary.chatgpt.dto.ChatMessage;
import adhd.diary.chatgpt.service.ChatGptService;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "일기 내용 감정 분석", description = "사용자가 작성한 일기 내용으로 감정을 분석하기 위해 사용하는 API")
    public CompletableFuture<ResponseEntity<String>> getAnalyzedDiary(@RequestParam String diaryContents) {
        return chatGptService.analyzeDiary(List.of(new ChatMessage(diaryContents + analyzeSentence)))
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage()));
    }

    @PostMapping("/chatgpt/recommend-song")
    @Operation(summary = "일기 내용 바탕 노래 추천", description = "사용자가 작성한 일기 내용을 바탕으로 노래를 추천받기 위해 사용하는 API")
    public CompletableFuture<ResponseEntity<String>> getRecommendSong(@RequestParam String diaryContents) {
        return chatGptService.recommendSong(List.of(new ChatMessage(diaryContents + recommendSongSentence)))
                .thenApply(ResponseEntity::ok)
                .exceptionally(e -> ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error : " + e.getMessage()));
    }
}
