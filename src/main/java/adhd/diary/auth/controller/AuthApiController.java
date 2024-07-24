package adhd.diary.auth.controller;

import adhd.diary.auth.service.AuthService;
import adhd.diary.member.dto.CompleteRegistrationRequest;
import adhd.diary.member.dto.MemberResponse;
import adhd.diary.response.ApiResponse;
import adhd.diary.response.ResponseCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/login")
public class AuthApiController {

    private final AuthService memberService;

    public AuthApiController(AuthService memberService) {
        this.memberService = memberService;
    }

    @PostMapping("/complete-registration")
    public ApiResponse<?> completeRegistration (@RequestBody CompleteRegistrationRequest completeRegistrationRequest, Principal principal) {

        MemberResponse memberResponse = memberService.completeRegistration(completeRegistrationRequest, principal.getName());

        return ApiResponse.success(ResponseCode.USER_CREATE_SUCCESS, memberResponse);
    }
}
