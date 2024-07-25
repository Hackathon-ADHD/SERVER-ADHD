package adhd.diary.member.controller;

import adhd.diary.member.dto.request.CompleteRegistrationRequest;
import adhd.diary.member.dto.response.MemberResponse;
import adhd.diary.member.dto.response.MemberSignUpResponse;
import adhd.diary.member.dto.request.UpdateNicknameRequest;
import adhd.diary.member.service.MemberService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
public class MemberApiController {

    private final MemberService memberService;

    public MemberApiController(MemberService memberService) {
        this.memberService = memberService;
    }

    @PutMapping("/api/update-nickname")
    public ResponseEntity<?> updateNickname(@RequestBody UpdateNicknameRequest updateNicknameRequest, Principal principal) {

        MemberResponse memberResponse = memberService.updateNickname(principal.getName(), updateNicknameRequest.getNickname());
        return ResponseEntity.ok().body(memberResponse);
    }

    @PostMapping("/login/complete-registration")
    public ResponseEntity<?> completeRegistration (@RequestBody CompleteRegistrationRequest completeRegistrationRequest, Principal principal) {

        MemberSignUpResponse memberSignUpResponse = memberService.completeRegistration(completeRegistrationRequest, principal.getName());
        return ResponseEntity.ok().body(memberSignUpResponse);
    }
}
