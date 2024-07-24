package adhd.diary.diary.service;

import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.DiaryRepository;
import adhd.diary.diary.dto.request.DiaryRequest;
import adhd.diary.diary.dto.response.DiaryResponse;
import adhd.diary.exception.DiaryException;
import adhd.diary.response.ResponseCode;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class DiaryService {

    private final DiaryRepository diaryRepository;

    public DiaryService(DiaryRepository diaryRepository) {
        this.diaryRepository = diaryRepository;
    }

    @Transactional
    public DiaryResponse save(DiaryRequest request) {
        Diary diary = diaryRepository.save(request.toEntity());
        return new DiaryResponse(diary);
    }

    @Transactional
    public DiaryResponse findById(Long id) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryException(ResponseCode.DIARY_NOT_FOUND));
        return new DiaryResponse(diary);
    }

    @Transactional
    public void deleteById(Long id) {
        Diary diary = diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryException(ResponseCode.DIARY_NOT_FOUND));
        diaryRepository.delete(diary);
    }

    @Transactional
    public DiaryResponse updateById(Long id, DiaryRequest request) {
        Diary existingDiary = diaryRepository.findById(id)
                .orElseThrow(() -> new DiaryException(ResponseCode.DIARY_NOT_FOUND));
        existingDiary.setContent(request.content());
        existingDiary.setEmotion(request.emotion());

        Diary diary = diaryRepository.save(existingDiary);

        return new DiaryResponse(diary);
    }
}
