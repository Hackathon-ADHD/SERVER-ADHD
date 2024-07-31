package adhd.diary.chatgpt.exception;

import adhd.diary.global.exception.BaseException;
import adhd.diary.response.ResponseCode;

public class ChatGptRetrievalException extends BaseException {

    public ChatGptRetrievalException(ResponseCode response) {
        super(response);
    }
}
