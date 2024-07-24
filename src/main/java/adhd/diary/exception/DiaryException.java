package adhd.diary.exception;

import adhd.diary.response.ResponseCode;

public class DiaryException extends BaseException{

    public DiaryException(ResponseCode responseCode) {
        super(responseCode);
    }
}
