package adhd.diary.diary.dto.response;

import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.Emotion;
import java.time.LocalDate;

public record DiaryDateResponse(Long id,
                                Emotion emotion,
                                LocalDate date) {
    public DiaryDateResponse(Diary diary) {
        this(
                diary.getId(),
                diary.getEmotion(),
                diary.getDate()
        );
    }
}
