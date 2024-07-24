package adhd.diary.auth.controller;

import adhd.diary.auth.dto.response.TokenResponse;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.member.domain.MemberRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public AuthController(JwtService jwtService, MemberRepository memberRepository) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
    }

    @PostMapping("/refresh")
    public ResponseEntity<TokenResponse> refreshAccessToken(HttpServletRequest request) {
        String authorizationHeader = request.getHeader("refreshToken");
        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            return ResponseEntity.badRequest().body(null);
        }

        String refreshToken = authorizationHeader.substring(7);

        if (!jwtService.isTokenValid(refreshToken)) {
            return ResponseEntity.badRequest().body(null);
        }

        TokenResponse tokenResponse = jwtService.refreshTokens(refreshToken);
        return ResponseEntity.ok(tokenResponse);
    }
}