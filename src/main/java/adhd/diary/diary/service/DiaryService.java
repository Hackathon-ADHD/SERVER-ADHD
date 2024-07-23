package adhd.diary.diary.service;

import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.DiaryRepository;
import adhd.diary.diary.dto.request.DiaryRequest;
import adhd.diary.diary.dto.response.DiaryResponse;
import org.springframework.stereotype.Service;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    public DiaryResponse save(DiaryRequest request) {
        Diary diary = diaryRepository.save(request.toEntity());
        return new DiaryResponse(diary);
    }

    public DiaryResponse findById(Long id) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일기장 정보를 찾을 수 없습니다."));
        return new DiaryResponse(diary);
    }

    public void deleteById(Long id) {
        diaryRepository.deleteById(id);
    }

    public DiaryResponse updateById(Long id, DiaryRequest request) {
        Diary existingDiary = diaryRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("일기장 정보를 찾을 수 없습니다."));
        existingDiary.setContent(request.content());
        existingDiary.setEmotion(request.emotion());

        Diary diary = diaryRepository.save(existingDiary);

        return new DiaryResponse(diary);
    }
}
