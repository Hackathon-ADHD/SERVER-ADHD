package adhd.diary.diary.controller;

import adhd.diary.diary.service.DiaryService;
import adhd.diary.diary.dto.request.DiaryRequest;
import adhd.diary.diary.dto.response.DiaryResponse;
import java.net.URI;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/diary")
    public ResponseEntity<DiaryResponse> create(@RequestBody DiaryRequest request) {
        DiaryResponse diaryResponse = diaryService.save(request);
        return ResponseEntity.created(URI.create("/diary")).body(diaryResponse);
    }

    @GetMapping("/diary/{id}")
    public ResponseEntity<DiaryResponse> diary(@PathVariable Long id) {
        DiaryResponse diaryResponse = diaryService.findById(id);
        return ResponseEntity.ok(diaryResponse);
    }

    @GetMapping("/diarys")
    public ResponseEntity<List<DiaryResponse>> diaryAll() {
        List<DiaryResponse> diaryResponses = diaryService.findAll();
        return ResponseEntity.ok().body(diaryResponses);
    }

    @DeleteMapping("/diary/{id}")
    public ResponseEntity<Void> remove(@PathVariable Long id) {
        diaryService.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/diary/{id}")
    public ResponseEntity<DiaryResponse> update(@PathVariable Long id, @RequestBody DiaryRequest request) {
        DiaryResponse diaryResponse = diaryService.updateById(id, request);
        return ResponseEntity.ok(diaryResponse);
    }
}