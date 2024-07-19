package adhd.diary.auth.controller;

import adhd.diary.auth.service.AuthService;
import adhd.diary.member.dto.CompleteRegistrationRequest;
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
    public ResponseEntity<?> completeRegistration (@RequestBody CompleteRegistrationRequest completeRegistrationRequest, Principal principal) {

          memberService.completeRegistration(completeRegistrationRequest, principal.getName());

        return ResponseEntity.ok().build();
    }
}
