package adhd.diary.auth.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class InvalidAuthorizationHeaderException extends BaseException {
    public InvalidAuthorizationHeaderException(ResponseCode responseCode) {
        super(responseCode);
    }
}
