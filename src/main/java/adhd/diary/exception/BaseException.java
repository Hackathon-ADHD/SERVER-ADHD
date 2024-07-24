package adhd.diary.exception;

import adhd.diary.response.ResponseCode;

public class BaseException extends IllegalArgumentException {

    private ResponseCode responseCode;

    public BaseException(ResponseCode responseCode) {
    }

    @Override
    public String getMessage() {
        return responseCode.getMessage();
    }
}
