package adhd.diary.diary.controller;

import adhd.diary.diary.application.DiaryService;
import adhd.diary.diary.dto.request.DiaryRequest;
import adhd.diary.diary.dto.response.DiaryResponse;
import java.net.URI;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/diary")
    public ResponseEntity<DiaryResponse> save(DiaryRequest request) {
        DiaryResponse diaryResponse = diaryService.save(request);
        return ResponseEntity.created(URI.create("/diary")).body(diaryResponse);
    }

}
