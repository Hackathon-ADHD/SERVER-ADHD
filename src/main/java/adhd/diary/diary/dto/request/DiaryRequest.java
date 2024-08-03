package adhd.diary.diary.dto.request;


import adhd.diary.diary.domain.Diary;
import adhd.diary.diary.domain.Emotion;
import adhd.diary.diary.exception.DiaryLocalDateConverterException;
import adhd.diary.response.ResponseCode;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

public record DiaryRequest(String content,
                           String analyzedContents,
                           String recommendSongs,
                           Emotion emotion,
                           String date) {

    public Diary toEntity() {
        return new Diary(
                content,
                analyzedContents,
                recommendSongs,
                emotion,
                toLocalDate(date)
        );
    }

    public LocalDate toLocalDate(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

        try {
            return LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            throw new DiaryLocalDateConverterException(ResponseCode.DIARY_CONVERTER_FAILED);
        }
    }
}
