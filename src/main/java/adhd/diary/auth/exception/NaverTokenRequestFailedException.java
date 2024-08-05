package adhd.diary.auth.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class NaverTokenRequestFailedException extends BaseException {

    public NaverTokenRequestFailedException(ResponseCode response) {
        super(response);
    }
}
