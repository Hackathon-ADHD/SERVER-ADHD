package adhd.diary.diary.dto.response;

import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.Emotion;
import java.time.LocalDate;
import java.time.LocalDateTime;

public record DiaryResponse(Long id,
                            String content,
                            String analyzedContents,
                            String recommendSongs,
                            Emotion emotion,
                            LocalDate date) {

    public DiaryResponse(Diary diary) {
        this(
                diary.getId(),
                diary.getContent(),
                diary.getAnalyzedContents(),
                diary.getRecommendSongs(),
                diary.getEmotion(),
                diary.getDate()
        );
    }
}
