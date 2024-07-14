package adhd.diary.diary.dto.request;


import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.Emotion;
import java.time.LocalDateTime;

public record DiaryRequest(String content,
                           Emotion emotion,
                           LocalDateTime localDateTime,
                           LocalDateTime modifiedTime) {

    public Diary toEntity() {
        return new Diary(
                content,
                emotion
        );
    }

}
