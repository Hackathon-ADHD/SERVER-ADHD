package adhd.diary.chatgpt.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class ChatGptDeserializationException extends BaseException {

    public ChatGptDeserializationException(ResponseCode response) {
        super(response);
    }
}
