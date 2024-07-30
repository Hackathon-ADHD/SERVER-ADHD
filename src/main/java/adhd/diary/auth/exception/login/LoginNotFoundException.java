package adhd.diary.auth.exception.login;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class LoginNotFoundException extends BaseException {

    public LoginNotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }
}
