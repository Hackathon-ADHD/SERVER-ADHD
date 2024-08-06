package adhd.diary.member.controller;

import adhd.diary.member.dto.request.CompleteRegistrationRequest;
import adhd.diary.member.dto.response.MemberResponse;
import adhd.diary.member.dto.response.MemberSignUpResponse;
import adhd.diary.member.dto.request.UpdateNicknameRequest;
import adhd.diary.member.service.MemberService;
import adhd.diary.response.ApiResponse;
import adhd.diary.response.ResponseCode;
import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api")
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PutMapping("/update-nickname")
    @Operation(summary = "사용자 닉네임 수정", description = "사용자가 닉네임을 수정하기 위해 사용하는 API")
    public ApiResponse<?> updateNickname(@RequestBody UpdateNicknameRequest updateNicknameRequest, Principal principal) {

        MemberResponse memberResponse = memberService.updateNickname(principal.getName(), updateNicknameRequest.getNickname());
        return ApiResponse.success(ResponseCode.NICKNAME_UPDATE_SUCCESS, memberResponse);
    }

    @PostMapping("/login/complete-registration")
    @Operation(summary = "사용자 회원가입 완료", description = "사용자의 회원가입을 완료하기 위해 사용하는 API")
    public ApiResponse<?> completeRegistration (@RequestBody CompleteRegistrationRequest completeRegistrationRequest, Principal principal) {

        MemberSignUpResponse memberSignUpResponse = memberService.completeRegistration(completeRegistrationRequest, principal.getName());
        return ApiResponse.success(ResponseCode.MEMBER_CREATE_SUCCESS, memberSignUpResponse);
    }
}