package adhd.diary.global.exception;

import adhd.diary.response.ResponseCode;

public class BaseException extends IllegalArgumentException {

    private ResponseCode responseCode;

    public BaseException(ResponseCode responseCode) {
        this.responseCode =responseCode;
    }

    public ResponseCode getResponseCode() {
        return responseCode;
    }

    @Override
    public String getMessage() {
        return responseCode.getMessage();
    }
}
