package adhd.diary.auth.exception.token;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class TokenNotFoundException extends BaseException {

    public TokenNotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }
}
