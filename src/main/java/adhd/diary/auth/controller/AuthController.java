package adhd.diary.auth.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

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