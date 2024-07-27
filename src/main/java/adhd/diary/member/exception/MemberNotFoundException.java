package adhd.diary.member.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class MemberNotFoundException extends BaseException {

    public MemberNotFoundException(ResponseCode responseCode) {
        super(responseCode);
    }
}
