package adhd.diary.diary.controller;

import adhd.diary.diary.dto.response.DiaryDateResponse;
import adhd.diary.diary.service.DiaryService;
import adhd.diary.diary.dto.request.DiaryRequest;
import adhd.diary.diary.dto.response.DiaryResponse;
import io.swagger.v3.oas.annotations.Operation;
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
    @Operation(summary = "일기 정보를 저장", description = "사용자가 일기를 작성하고 분석까지 진행한 후 일기 정보를 저장하기 위해 사용하는 API")
    public ApiResponse<DiaryResponse> create(@RequestBody DiaryRequest request) {
        DiaryResponse diaryResponse = diaryService.save(request);
        return ApiResponse.success(ResponseCode.DIARY_CREATE_SUCCESS, diaryResponse);
    }

    @GetMapping("/diary/{id}")
    @Operation(summary = "일기 정보를 조회", description = "사용자가 일기의 상세 정보를 조회하기 위해 사용하는 API")
    public ApiResponse<DiaryResponse> diary(@PathVariable Long id) {
        DiaryResponse diaryResponse = diaryService.findById(id);
        return ApiResponse.success(ResponseCode.DIARY_READ_BY_ID_SUCCESS, diaryResponse);
    }

    @GetMapping("/diary/dates")
    @Operation(summary = "일기 날짜, 내용, 감정 정보를 조회", description = "달력에 감정을 표현하기 위해 사용하는 API")
    public ApiResponse<List<DiaryDateResponse>> diaryDate() {
        List<DiaryDateResponse> diaryDateResponse = diaryService.findDatesByEmail();
        return ApiResponse.success(ResponseCode.DIARY_READ_BY_ID_SUCCESS, diaryDateResponse);
    }

    @GetMapping("/diary/last-year")
    @Operation(summary = "작년 일기 정보를 조회", description = "보내준 날짜를 기준으로 1년 전의 일기 정보를 조회하기 위해 사용하는 API")
    public ApiResponse<DiaryResponse> getDiaryFromLastYear(@RequestParam String date) {
        DiaryResponse lastYearDiary = diaryService.findLastYearDiary(date);
        return ApiResponse.success(ResponseCode.DIARY_READ_BY_YEAR_SUCCESS, lastYearDiary);
    }

    @DeleteMapping("/diary/{id}")
    @Operation(summary = "일기 정보를 삭제", description = "사용자가 일기의 상세 정보를 삭제하기 위해 사용하는 API")
    public ApiResponse<Void> remove(@PathVariable Long id) {
        diaryService.deleteById(id);
        return ApiResponse.success(ResponseCode.DIARY_DELETE_SUCCESS);
    }

    @PutMapping("/diary/{id}")
    @Operation(summary = "일기 정보를 수정", description = "사용자가 일기의 상세 정보를 수정하기 위해 사용하는 API")
    public ApiResponse<DiaryResponse> update(@PathVariable Long id, @RequestBody DiaryRequest request) {
        DiaryResponse diaryResponse = diaryService.updateById(id, request);
        return ApiResponse.success(ResponseCode.DIARY_UPDATE_SUCCESS, diaryResponse);
    }
}