package adhd.diary.member.controller;

import adhd.diary.member.dto.request.CompleteRegistrationRequest;
import adhd.diary.member.dto.response.MemberResponse;
import adhd.diary.member.dto.response.MemberSignUpResponse;
import adhd.diary.member.dto.request.UpdateNicknameRequest;
import adhd.diary.member.service.MemberService;
import adhd.diary.response.ApiResponse;
import adhd.diary.response.ResponseCode;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PutMapping("/api/update-nickname")
    public ApiResponse<?> updateNickname(@RequestBody UpdateNicknameRequest updateNicknameRequest, Principal principal) {

        MemberResponse memberResponse = memberService.updateNickname(principal.getName(), updateNicknameRequest.getNickname());
        return ApiResponse.success(ResponseCode.NICKNAME_UPDATE_SUCCESS, memberResponse);
    }

    @PostMapping("/login/complete-registration")
    public ApiResponse<?> completeRegistration (@RequestBody CompleteRegistrationRequest completeRegistrationRequest, Principal principal) {

        MemberSignUpResponse memberSignUpResponse = memberService.completeRegistration(completeRegistrationRequest, principal.getName());
        return ApiResponse.success(ResponseCode.MEMBER_CREATE_SUCCESS, memberSignUpResponse);
    }
}