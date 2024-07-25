package adhd.diary.auth.jwt;

import adhd.diary.auth.dto.response.TokenResponse;
import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.MemberRepository;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.exceptions.TokenExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class JwtService {

    @Value("${jwt.secretKey}")
    private String secretKey;

    @Value("${jwt.access.expiration}")
    private Long accessTokenExpirationPeriod;

    @Value("${jwt.refresh.expiration}")
    private Long refreshTokenExpirationPeriod;

    private static final String ACCESS_TOKEN_SUBJECT = "AccessToken";
    private static final String REFRESH_TOKEN_SUBJECT = "RefreshToken";
    private static final String EMAIL_CLAIM = "email";
    private static final String AUTHORIZATION = "Authorization";
    private static final String AUTHORIZATION_REFRESH_TOKEN = "refreshToken";

    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;

    public JwtService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    public String createAccessToken(String email) {
        long now = (new Date()).getTime();
        Date accessTokenExpiresIn = new Date(now + accessTokenExpirationPeriod);

        return JWT.create()
                .withSubject(ACCESS_TOKEN_SUBJECT)
                .withExpiresAt(accessTokenExpiresIn)
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public String createRefreshToken(String email) {
        long now = (new Date()).getTime();
        Date refreshTokenExpiresIn = new Date(now + refreshTokenExpirationPeriod);

        return JWT.create()
                .withSubject(REFRESH_TOKEN_SUBJECT)
                .withExpiresAt(refreshTokenExpiresIn)
                .withClaim(EMAIL_CLAIM, email)
                .sign(Algorithm.HMAC512(secretKey));
    }

    public void sendAccessToken(HttpServletResponse response, String accessToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
    }

    public void sendAccessAndRefreshToken(HttpServletResponse response,
                                          String accessToken,
                                          String refreshToken) {
        response.setStatus(HttpServletResponse.SC_OK);
        setAccessTokenHeader(response, accessToken);
        setRefreshTokenHeader(response, refreshToken);
    }

    public Optional<String> extractRefreshToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION_REFRESH_TOKEN))
                .filter(refreshToken -> refreshToken.startsWith(BEARER))
                .map(refreshToken -> refreshToken.replace(BEARER, ""));
    }

    public Optional<String> extractAccessToken(HttpServletRequest request) {
        return Optional.ofNullable(request.getHeader(AUTHORIZATION))
                .filter(accessToken -> accessToken.startsWith(BEARER))
                .map(accessToken -> accessToken.replace(BEARER, ""));
    }

    public Optional<String> extractEmail(String accessToken) {
        try {
            return Optional.ofNullable(JWT.require(Algorithm.HMAC512(secretKey))
                    .build()
                    .verify(accessToken)
                    .getClaim(EMAIL_CLAIM)
                    .asString());
        }catch (Exception e) {
            throw new IllegalArgumentException("엑세스 토큰이 유효하지 않습니다.");
        }
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(AUTHORIZATION, BEARER + accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(AUTHORIZATION_REFRESH_TOKEN, BEARER + refreshToken);
    }

    public void updateRefreshToken(String email, String refreshToken) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new IllegalArgumentException("일치하는 회원이 없습니다."));
        member.updateRefreshToken(refreshToken);
        memberRepository.saveAndFlush(member);
    }

    public boolean isTokenValid(String token) {
        try {
            var verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
            var decodedJWT = verifier.verify(token);
            return true;
        } catch (TokenExpiredException e) {
            System.err.println("토큰이 만료되었습니다: " + e.getMessage());
        } catch (JWTVerificationException e) {
            System.err.println("토큰 검증 실패: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("예상치 못한 오류 발생: " + e.getMessage());
        }
        throw new IllegalArgumentException("유효하지 않은 토큰입니다");
    }

    public Authentication getAuthentication(String accessToken) {
        String email = extractEmail(accessToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 액세스 토큰입니다"));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다"));

        List<SimpleGrantedAuthority> authorities = Collections.singletonList(
                new SimpleGrantedAuthority(member.getRole().getKey())
        );

        UserDetails principal = User.builder()
                .username(member.getEmail())
                .password("")
                .authorities(authorities)
                .build();

        return new UsernamePasswordAuthenticationToken(principal, "", principal.getAuthorities());
    }

    public String getTokenType(String token) {
        try {
            var verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
            var decodedJWT = verifier.verify(token);
            String subject = decodedJWT.getSubject();

            if (ACCESS_TOKEN_SUBJECT.equals(subject)) {
                return "AccessToken";
            } else if (REFRESH_TOKEN_SUBJECT.equals(subject)) {
                return "RefreshToken";
            } else {
                throw new IllegalArgumentException("알 수 없는 토큰 유형입니다.");
            }
        } catch (JWTVerificationException e) {
            throw new IllegalArgumentException("토큰 검증에 실패했습니다: " + e.getMessage(), e);
        } catch (Exception e) {
            throw new IllegalArgumentException("예상치 못한 오류가 발생했습니다: " + e.getMessage(), e);
        }
    }

    public TokenResponse refreshTokens(String refreshToken) {
        System.out.println("Received refreshToken: " + refreshToken);

        if (!isTokenValid(refreshToken)) {
            System.err.println("Invalid refreshToken");
            throw new IllegalArgumentException("유효하지 않은 refresh token입니다.");
        }

        String email = extractEmail(refreshToken)
                .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 refresh token입니다."));
        System.out.println("Extracted email: " + email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("회원을 찾을 수 없습니다."));

        String newAccessToken = createAccessToken(email);
        String newRefreshToken = createRefreshToken(email);
        updateRefreshToken(email, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }
}
