package adhd.diary.chatgpt.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class ChatGptJsonParsingException extends BaseException {

    public ChatGptJsonParsingException(ResponseCode response) {
        super(response);
    }
}
