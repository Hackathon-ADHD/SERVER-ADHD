package adhd.diary.member.exception;

import adhd.diary.response.ErrorResponse;
import adhd.diary.response.ResponseCode;
import java.time.LocalDateTime;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;

@RestControllerAdvice
public class MemberExceptionHandler {

    @ExceptionHandler(MemberNotFoundException.class)
    public ResponseEntity<ErrorResponse> handleMemberNotFoundException(MemberNotFoundException exception, WebRequest request) {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                exception.getResponseCode().name(),
                exception.getResponseCode().getMessage(),
                request.getDescription(false)
        );
        return new ResponseEntity<>(errorResponse, exception.getResponseCode().getHttpStatus());
    }
}
