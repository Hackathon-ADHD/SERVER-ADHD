package adhd.diary.auth.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class KakaoTokenRequestFailedException extends BaseException {

    public KakaoTokenRequestFailedException(ResponseCode response) {
        super(response);
    }
}
