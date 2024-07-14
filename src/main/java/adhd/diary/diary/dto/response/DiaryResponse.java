package adhd.diary.diary.dto.response;

import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.Emotion;
import java.time.LocalDateTime;

public record DiaryResponse(Long id,
                            String content,
                            Emotion emotion,
                            LocalDateTime localDateTime,
                            LocalDateTime modifiedTime) {

    public DiaryResponse(Diary diary) {
        this(
                diary.getId(),
                diary.getContent(),
                diary.getEmotion(),
                diary.getCreatedDate(),
                diary.getModifiedDate()
        );
    }

}
