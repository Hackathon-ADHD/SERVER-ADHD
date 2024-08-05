package adhd.diary.auth.controller;

import adhd.diary.auth.jwt.JwtBlacklistService;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.member.dto.response.MemberLogoutResponse;
import adhd.diary.member.service.MemberService;
import adhd.diary.response.ApiResponse;
import adhd.diary.response.ResponseCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.Principal;

@RestController
public class LogoutApiController {

    private static final Logger logger = LoggerFactory.getLogger(LogoutApiController.class);
    private final JwtBlacklistService jwtBlacklistService;
    private final JwtService jwtService;
    private final MemberService memberService;

    public LogoutApiController(JwtBlacklistService jwtBlacklistService, JwtService jwtService, MemberService memberService) {
        this.jwtBlacklistService = jwtBlacklistService;
        this.jwtService = jwtService;
        this.memberService = memberService;
    }

    @PostMapping("/api/logout")
    public ApiResponse<?> logout(Principal principal, @RequestHeader("Authorization") String authorizationHeader) {
        logger.info("로그아웃 요청 받음");

        String accessToken = jwtService.extractAccessTokenFromHeader(authorizationHeader);

        if (accessToken == null) {
            logger.warn("요청 헤더에서 액세스 토큰을 찾을 수 없음.");
            return ApiResponse.fail(ResponseCode.JWT_ACCESS_TOKEN_EXPIRED);
        }

        if (jwtBlacklistService.isTokenBlacklisted(accessToken)) {
            logger.warn("이미 블랙리스트에 있는 액세스 토큰입니다.");
            return ApiResponse.success(ResponseCode.LOGOUT_SUCCESS, "이미 로그아웃됨");
        }

        if (!jwtService.isTokenValid(accessToken)) {
            logger.warn("유효하지 않은 액세스 토큰: {}", accessToken);
            return ApiResponse.fail(ResponseCode.JWT_ACCESS_TOKEN_EXPIRED);
        }

        MemberLogoutResponse memberLogoutResponse = memberService.logout(principal.getName());
        if (memberLogoutResponse == null) {
            logger.warn("이메일 {}로 회원을 찾을 수 없음", principal.getName());
            return ApiResponse.fail(ResponseCode.LOGOUT_FAILED);
        }

        try {
            String refreshToken = memberLogoutResponse.getRefreshToken();

            jwtBlacklistService.addTokenToBlacklist(accessToken);
            if (refreshToken != null) {
                jwtBlacklistService.addTokenToBlacklist(refreshToken);
            }

            logger.info("로그아웃 성공");
            return ApiResponse.success(ResponseCode.LOGOUT_SUCCESS, "ok");
        } catch (Exception e) {
            logger.error("로그아웃 실패: 사용자 {}", principal.getName(), e);
            return ApiResponse.fail(ResponseCode.LOGOUT_FAILED);
        }
    }
}