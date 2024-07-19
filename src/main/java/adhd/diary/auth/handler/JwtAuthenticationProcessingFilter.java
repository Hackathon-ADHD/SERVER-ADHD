package adhd.diary.auth.handler;

import adhd.diary.auth.jwt.JwtService;
import adhd.diary.member.domain.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;

    public JwtAuthenticationProcessingFilter(JwtService jwtService, MemberRepository memberRepository) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        String refreshToken = jwtService.extractRefreshToken(request).orElse(null);

        System.out.println("===================");
        System.out.println("accessToken == " + accessToken);
        System.out.println("refreshToken == " + refreshToken);
        System.out.println("===================");


        if (accessToken != null && jwtService.isTokenValid(accessToken)) {
            System.out.println("step1 success");
            Authentication authentication = jwtService.getAuthentication(accessToken);
            System.out.println("step2 success");
            SecurityContextHolder.getContext().setAuthentication(authentication);
        } else if (refreshToken != null) {
            System.out.println("step3 success");
            memberRepository.findByRefreshToken(refreshToken).ifPresent(member -> {
                String newAccessToken = jwtService.createAccessToken(member.getEmail());
                jwtService.sendAccessToken(response, newAccessToken);
                Authentication authentication = jwtService.getAuthentication(newAccessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            });
        }

        filterChain.doFilter(request, response);
    }
}