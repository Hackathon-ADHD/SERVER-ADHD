package adhd.diary.auth.exception.token;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class TokenInvalidException extends BaseException {

    public TokenInvalidException(ResponseCode responseCode) {
        super(responseCode);
    }
}
