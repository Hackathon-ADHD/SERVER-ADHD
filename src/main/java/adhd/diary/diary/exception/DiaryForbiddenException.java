package adhd.diary.diary.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class DiaryForbiddenException extends BaseException {

    public DiaryForbiddenException(ResponseCode responseCode) {
        super(responseCode);
    }
}
