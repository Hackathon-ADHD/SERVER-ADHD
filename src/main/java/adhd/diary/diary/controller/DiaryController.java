package adhd.diary.diary.controller;

import adhd.diary.diary.dto.response.DiaryDateResponse;
import adhd.diary.diary.service.DiaryService;
import adhd.diary.diary.dto.request.DiaryRequest;
import adhd.diary.diary.dto.response.DiaryResponse;
import java.util.List;

import adhd.diary.response.ApiResponse;
import adhd.diary.response.ResponseCode;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class DiaryController {

    private final DiaryService diaryService;

    public DiaryController(DiaryService diaryService) {
        this.diaryService = diaryService;
    }

    @PostMapping("/diary")
    public ApiResponse<DiaryResponse> create(@RequestBody DiaryRequest request) {
        DiaryResponse diaryResponse = diaryService.save(request);
        return ApiResponse.success(ResponseCode.DIARY_CREATE_SUCCESS, diaryResponse);
    }

    @GetMapping("/diary/{id}")
    public ApiResponse<DiaryResponse> diary(@PathVariable Long id) {
        DiaryResponse diaryResponse = diaryService.findById(id);
        return ApiResponse.success(ResponseCode.DIARY_READ_BY_ID_SUCCESS, diaryResponse);
    }

    @GetMapping("/diary/date/{id}")
    public ApiResponse<DiaryDateResponse> diaryDate(@PathVariable Long id) {
        DiaryDateResponse diaryDateResponse = diaryService.findDateById(id);
        return ApiResponse.success(ResponseCode.DIARY_READ_BY_ID_SUCCESS, diaryDateResponse);
    }

    @GetMapping("/diarys")
    public ApiResponse<List<DiaryResponse>> diaryAll() {
        List<DiaryResponse> diaryResponses = diaryService.findAll();
        return ApiResponse.success(ResponseCode.DIARY_READ_ALL_SUCCESS, diaryResponses);
    }

    @DeleteMapping("/diary/{id}")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        diaryService.deleteById(id);
        return ApiResponse.success(ResponseCode.DIARY_DELETE_SUCCESS);
    }

    @PutMapping("/diary/{id}")
    public ApiResponse<DiaryResponse> update(@PathVariable Long id, @RequestBody DiaryRequest request) {
        DiaryResponse diaryResponse = diaryService.updateById(id, request);
        return ApiResponse.success(ResponseCode.DIARY_UPDATE_SUCCESS, diaryResponse);
    }
}