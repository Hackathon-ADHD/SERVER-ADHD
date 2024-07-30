package adhd.diary.response;

import org.springframework.http.HttpStatus;

public enum ResponseCode {

    // 400 Bad Request
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "잘못된 요청입니다."),

    // 403 Forbidden
    FORBIDDEN(HttpStatus.FORBIDDEN, "권한이 없습니다."),

    // 405 Method Not Allowed
    METHOD_NOT_ALLOWED(HttpStatus.METHOD_NOT_ALLOWED, "허용되지 않은 메소드입니다."),

    // 500 Internal Server Error
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버에 오류가 발생하였습니다."),

    /**
     * User response
     */
    // 404 Not Found
    MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "사용자를 찾을 수 없습니다,"),

    // 200 OK
    MEMBER_READ_SUCCESS(HttpStatus.OK, "사용자 정보 조회 성공"),
    KAKAO_LOGIN_SUCCESS(HttpStatus.OK, "카카오 로그인 성공"),
    NAVER_LOGIN_SUCCESS(HttpStatus.OK, "네이버 로그인 성공"),
    GOOGLE_LOGIN_SUCCESS(HttpStatus.OK, "구글 로그인 성공"),
    NICKNAME_UPDATE_SUCCESS(HttpStatus.OK, "닉네임 수정 성공"),

    // 201 Created
    MEMBER_CREATE_SUCCESS(HttpStatus.CREATED, "회원가입 성공"),

    /**
     * Diary response
     */
    // 404 Not Found
    DIARY_NOT_FOUND(HttpStatus.NOT_FOUND, "일기를 찾을 수 없습니다."),
    DIARY_FORBIDDEN(HttpStatus.FORBIDDEN, "일기장에 접근할 권한이 없습니다."),

    // 200 OK
    DIARY_READ_ALL_SUCCESS(HttpStatus.OK, "일기 전체 조회 성공"),
    DIARY_READ_BY_ID_SUCCESS(HttpStatus.OK, "일기 조회 성공"),
    DIARY_UPDATE_SUCCESS(HttpStatus.OK, "일기 수정 성공"),
    DIARY_DELETE_SUCCESS(HttpStatus.OK, "일기 삭제 성공"),

    // 201 Created
    DIARY_CREATE_SUCCESS(HttpStatus.CREATED, "일기 생성 성공"),

    // JWT Token errors
    JWT_ACCESS_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "자체 JWT ACCESS 토큰이 만료되었습니다."),
    JWT_REFRESH_TOKEN_EXPIRED(HttpStatus.UNAUTHORIZED, "자체 JWT REFRESH 토큰이 만료되었습니다."),

    JWT_TOKEN_RETRIEVAL_FAILED(HttpStatus.UNAUTHORIZED, "자체 JWT 토큰 가져오기에 실패하였습니다."),
    UNKNOWN_TOKEN_TYPE(HttpStatus.BAD_REQUEST, "알 수 없는 토큰 유형입니다."),
    TOKEN_VALIDATION_FAILED(HttpStatus.UNAUTHORIZED, "토큰 검증에 실패했습니다."),
    UNEXPECTED_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 오류가 발생했습니다."),

    // OAuth2 Token retrieval errors
    KAKAO_TOKEN_RETRIEVAL_FAILED(HttpStatus.UNAUTHORIZED, "카카오 토큰 가져오기에 실패하였습니다."),
    GOOGLE_TOKEN_RETRIEVAL_FAILED(HttpStatus.UNAUTHORIZED, "구글 토큰 가져오기에 실패하였습니다."),
    NAVER_TOKEN_RETRIEVAL_FAILED(HttpStatus.UNAUTHORIZED, "네이버 토큰 가져오기에 실패하였습니다."),

    // OAuth2 User info retrieval errors
    KAKAO_USER_INFO_RETRIEVAL_FAILED(HttpStatus.UNAUTHORIZED, "카카오 사용자 정보를 가져오는데 실패하였습니다."),
    GOOGLE_USER_INFO_RETRIEVAL_FAILED(HttpStatus.UNAUTHORIZED, "구글 사용자 정보를 가져오는데 실패하였습니다."),
    NAVER_USER_INFO_RETRIEVAL_FAILED(HttpStatus.UNAUTHORIZED, "네이버 사용자 정보를 가져오는데 실패하였습니다.");

    private final HttpStatus httpStatus;
    private final String message;

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