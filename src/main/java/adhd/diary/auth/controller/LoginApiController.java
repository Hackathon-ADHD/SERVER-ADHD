package adhd.diary.auth.controller;

import adhd.diary.auth.dto.response.SocialLoginResponse;
import adhd.diary.auth.service.KakaoService;
import adhd.diary.auth.service.NaverService;
import adhd.diary.response.ApiResponse;
import adhd.diary.response.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginApiController {

    private final KakaoService kakaoService;
    private final NaverService naverService;

    public LoginApiController(KakaoService kakaoService, NaverService naverService) {
        this.kakaoService = kakaoService;
        this.naverService = naverService;
    }

    @GetMapping
    public String login() {
        return "로그인 페이지입니다.";
    }

    @GetMapping("/kakao")
    @Operation(summary = "카카오 소셜 로그인", description = "사용자가 카카오 소셜로그인을 하기 위해 사용하는 API")
    public ApiResponse<SocialLoginResponse> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        String accessToken = kakaoService.getKakaoAccessToken(code);
        SocialLoginResponse socialLoginResponse = kakaoService.kakaoLogin(accessToken);
        return ApiResponse.success(ResponseCode.KAKAO_LOGIN_SUCCESS, socialLoginResponse);
    }

    @GetMapping("/naver")
    @Operation(summary = "네이버 소셜 로그인", description = "사용자가 네이버 소셜로그인을 하기 위해 사용하는 API")
    public ApiResponse<SocialLoginResponse> naverLogin(@RequestParam String code) throws JsonProcessingException {
        String accessToken = naverService.getNaverAccessToken(code);
        SocialLoginResponse socialLoginResponse = naverService.naverLogin(accessToken);
        return ApiResponse.success(ResponseCode.NAVER_LOGIN_SUCCESS, socialLoginResponse);
    }
}
