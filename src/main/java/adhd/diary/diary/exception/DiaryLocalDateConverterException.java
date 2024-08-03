package adhd.diary.diary.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class DiaryLocalDateConverterException extends BaseException {

    public DiaryLocalDateConverterException(ResponseCode response) {
        super(response);
    }
}
