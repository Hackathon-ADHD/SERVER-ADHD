package adhd.diary.auth.handler;

import adhd.diary.auth.CustomOAuth2User;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.member.domain.Role;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class OAuth2LoginSuccessHandler implements AuthenticationSuccessHandler {

    private final static String AUTHORIZATION = "Authorization";
    private final JwtService jwtService;

    public OAuth2LoginSuccessHandler(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        CustomOAuth2User oAuth2User = (CustomOAuth2User) authentication.getPrincipal();

        String accessToken = jwtService.createAccessToken(oAuth2User.getEmail());
        String refreshToken = jwtService.createRefreshToken(oAuth2User.getEmail());
        System.out.println("accessToken = " + accessToken);
        System.out.println("refreshToken = " + refreshToken);

        jwtService.updateRefreshToken(oAuth2User.getEmail(), refreshToken);

        jwtService.sendAccessAndRefreshToken(response, accessToken, refreshToken);

        if (oAuth2User.getRole() == Role.USER) {
            response.sendRedirect("signup");
        } else {
            response.sendRedirect("/");
        }
    }
}