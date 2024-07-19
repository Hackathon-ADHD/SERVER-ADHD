package adhd.diary.auth.controller;

import adhd.diary.member.dto.CompleteRegistrationRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDate;

@Controller
public class AuthController {

    @GetMapping("/login/oauth2/code/signup")
    public String redirectNicknameForm() {
        return "redirect:/signup/nickname";
    }
    @GetMapping("/signup/nickname")
    public String showNicknameForm() {
        return "nickname";
    }

    @GetMapping("/signup/birthday")
    public String saveBirthday() {

        return "birthday";
    }

}
