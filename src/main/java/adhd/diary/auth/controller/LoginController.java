package adhd.diary.auth.controller;

import adhd.diary.auth.dto.response.SocialLoginResponse;
import adhd.diary.auth.service.KakaoService;
import adhd.diary.auth.service.NaverService;
import adhd.diary.response.ApiResponse;
import adhd.diary.response.ResponseCode;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/login")
public class LoginController {

    private final KakaoService kakaoService;
    private final NaverService naverService;

    public LoginController(KakaoService kakaoService, NaverService naverService) {
        this.kakaoService = kakaoService;
        this.naverService = naverService;
    }

    @GetMapping("/kakao")
    public ApiResponse<SocialLoginResponse> kakaoLogin(@RequestParam String code) throws JsonProcessingException {
        String accessToken = kakaoService.getKakaoAccessToken(code);
        SocialLoginResponse socialLoginResponse = kakaoService.kakaoLogin(accessToken);
        return ApiResponse.success(ResponseCode.KAKAO_LOGIN_SUCCESS, socialLoginResponse);
    }

    @GetMapping("/naver")
    public ApiResponse<SocialLoginResponse> naverLogin(@RequestParam String code) throws JsonProcessingException {
        String accessToken = naverService.getNaverAccessToken(code);
        SocialLoginResponse socialLoginResponse = naverService.naverLogin(accessToken);
        return ApiResponse.success(ResponseCode.NAVER_LOGIN_SUCCESS, socialLoginResponse);
    }
}
