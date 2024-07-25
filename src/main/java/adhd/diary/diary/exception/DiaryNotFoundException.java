package adhd.diary.diary.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class DiaryNotFoundException extends BaseException {

    public DiaryNotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }
}
