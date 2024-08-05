package adhd.diary.diary.dto.response;

import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.Emotion;
import java.time.LocalDate;

public record DiaryDateResponse(Long id,
                                String content,
                                Emotion emotion,
                                String date) {
    public DiaryDateResponse(Diary diary) {
        this(
                diary.getId(),
                diary.getContent(),
                diary.getEmotion(),
                convertToString(diary.getDate())
        );
    }

    public static String convertToString(LocalDate date) {
        return date.toString();
    }
}
