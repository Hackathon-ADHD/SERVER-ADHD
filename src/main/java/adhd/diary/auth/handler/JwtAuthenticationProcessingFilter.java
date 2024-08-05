package adhd.diary.auth.handler;

import adhd.diary.auth.jwt.JwtBlacklistService;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.member.domain.MemberRepository;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationProcessingFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;
    private final JwtBlacklistService jwtBlacklistService;

    public JwtAuthenticationProcessingFilter(JwtService jwtService,
                                             MemberRepository memberRepository,
                                             RedisTemplate<String, Object> redisTemplate,
                                             JwtBlacklistService jwtBlacklistService) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
        this.redisTemplate = redisTemplate;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {
        String accessToken = jwtService.extractAccessToken(request).orElse(null);
        String refreshToken = jwtService.extractRefreshToken(request).orElse(null);

        if (accessToken != null && jwtService.isTokenValid(accessToken)) {
            String isLogout = (String) redisTemplate.opsForValue().get(accessToken);

            if (ObjectUtils.isEmpty(isLogout)) {
                Authentication authentication = jwtService.getAuthentication(accessToken);
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } else if (refreshToken != null) {
            if (jwtService.isTokenValid(refreshToken)) {
                memberRepository.findByRefreshToken(refreshToken).ifPresent(member -> {
                    String newAccessToken = jwtService.createAccessToken(member.getEmail());
                    String newRefreshToken = jwtService.createRefreshToken(member.getEmail());

                    jwtBlacklistService.addTokenToBlacklist(refreshToken);
                    redisTemplate.delete(refreshToken);
                    redisTemplate.opsForValue().set(newRefreshToken, member.getEmail());

                    jwtService.sendAccessAndRefreshToken(response, newAccessToken, newRefreshToken);

                    Authentication authentication = jwtService.getAuthentication(newAccessToken);
                    SecurityContextHolder.getContext().setAuthentication(authentication);
                });
            } else {
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Refresh Token Expired");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}