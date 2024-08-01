package adhd.diary.auth.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class KakaoMemberRequestFailedException extends BaseException {

    public KakaoMemberRequestFailedException(ResponseCode response) {
        super(response);
    }
}
