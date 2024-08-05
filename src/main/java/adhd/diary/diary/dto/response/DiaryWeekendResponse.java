package adhd.diary.diary.dto.response;

import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.Emotion;
import java.time.LocalDate;

public record DiaryWeekendResponse(Long id,
                                   Emotion emotion,
                                   int score,
                                   String date) {
    public DiaryWeekendResponse(Diary diary) {
        this(
                diary.getId(),
                diary.getEmotion(),
                diary.getEmotion().getScore(),
                convertToString(diary.getDate())
        );
    }

    public static String convertToString(LocalDate date) {
        return date.toString();
    }

}
