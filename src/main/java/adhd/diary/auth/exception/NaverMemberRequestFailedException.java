package adhd.diary.auth.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class NaverMemberRequestFailedException extends BaseException {

    public NaverMemberRequestFailedException(ResponseCode response) {
        super(response);
    }
}
