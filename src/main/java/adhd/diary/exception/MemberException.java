package adhd.diary.exception;

import adhd.diary.response.ResponseCode;

public class MemberException extends BaseException{

    public MemberException(ResponseCode responseCode) {
        super(responseCode);
    }
}
