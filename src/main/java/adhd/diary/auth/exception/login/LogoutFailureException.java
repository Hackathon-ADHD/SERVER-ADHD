package adhd.diary.auth.exception.login;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class LogoutFailureException extends BaseException {

    public LogoutFailureException(ResponseCode responseCode) {
        super(responseCode);
    }
}
