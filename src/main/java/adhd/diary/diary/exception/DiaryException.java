package adhd.diary.diary.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class DiaryException extends BaseException {

    public DiaryException(ResponseCode responseCode) {
        super(responseCode);
    }
}
