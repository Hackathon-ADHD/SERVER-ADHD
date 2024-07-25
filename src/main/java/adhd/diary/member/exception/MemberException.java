package adhd.diary.member.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class MemberException extends BaseException {

    public MemberException(ResponseCode responseCode) {
        super(responseCode);
    }
}
