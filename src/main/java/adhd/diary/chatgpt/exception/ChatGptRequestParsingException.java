package adhd.diary.chatgpt.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class ChatGptRequestParsingException extends BaseException {

    public ChatGptRequestParsingException(ResponseCode response) {
        super(response);
    }
}
