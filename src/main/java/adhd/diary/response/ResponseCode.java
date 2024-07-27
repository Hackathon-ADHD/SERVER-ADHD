package adhd.diary.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    //400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    //403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    //405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드입니다."),

    //500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다."),

    /**
     * user response
     */
    //404 Not Found
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다,"),

    //200 OK
    MEMBER_READ_SUCCESS(HttpStatus.OK, "사용자 정보 조회 성공"),
    MEMBER_LOGIN_SUCCESS(HttpStatus.OK, "로그인 성공"),

    //201 Created
    MEMBER_CREATE_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),

    /**
     * diary response
     */
    //404 Not Found
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "일기를 찾을 수 없습니다."),
    DIARY_FORBIDDEN(HttpStatus.FORBIDDEN, "일기장에 접근할 권한이 없습니다."),

    //200 OK
    DIARY_READ_ALL_SUCCESS(HttpStatus.OK, "일기 전체 조회 성공"),
    DIARY_READ_BY_ID_SUCCESS(HttpStatus.OK, "일기 조회 성공"),
    DIARY_UPDATE_SUCCESS(HttpStatus.OK, "일기 수정 성공"),
    DIARY_DELETE_SUCCESS(HttpStatus.OK, "일기 삭제 성공"),

    //201 Created
    DIARY_CREATE_SUCCESS(HttpStatus.CREATED, "일기 생성 성공");

    private HttpStatus httpStatus;
    private String message;

    ResponseCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }

    public HttpStatus getHttpStatus() {
        return httpStatus;
    }

    public String getMessage() {
        return message;
    }
}
