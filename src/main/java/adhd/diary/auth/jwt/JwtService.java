package adhd.diary.auth.jwt;

import adhd.diary.auth.dto.response.TokenResponse;
import adhd.diary.auth.exception.token.TokenNotFoundException;
import adhd.diary.member.domain.Member;
import adhd.diary.member.domain.MemberRepository;
import adhd.diary.member.exception.MemberNotFoundException;
import adhd.diary.response.ResponseCode;
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
    private static final String AUTHORIZATION_REFRESH_TOKEN = "RefreshToken";

    private static final String BEARER = "Bearer ";

    private final MemberRepository memberRepository;
    private JwtBlacklistService jwtBlacklistService;

    public JwtService(MemberRepository memberRepository, JwtBlacklistService jwtBlacklistService) {
        this.memberRepository = memberRepository;
        this.jwtBlacklistService = jwtBlacklistService;
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
            throw new TokenNotFoundException(ResponseCode.JWT_ACCESS_TOKEN_EXPIRED);
        }
    }

    public void setAccessTokenHeader(HttpServletResponse response, String accessToken) {
        response.setHeader(AUTHORIZATION, BEARER + accessToken);
    }

    public void setRefreshTokenHeader(HttpServletResponse response, String refreshToken) {
        response.setHeader(AUTHORIZATION_REFRESH_TOKEN, BEARER + refreshToken);
    }

    public void updateRefreshToken(String email, String refreshToken) {
        Member member = memberRepository.findByEmail(email).orElseThrow(() -> new MemberNotFoundException(ResponseCode.MEMBER_NOT_FOUND));
        member.updateRefreshToken(refreshToken);
        memberRepository.saveAndFlush(member);
    }

    public boolean isTokenValid(String token) {
        try {
            if (jwtBlacklistService.isTokenBlacklisted(token)) {
                return false;
            }
            var verifier = JWT.require(Algorithm.HMAC512(secretKey)).build();
            verifier.verify(token);
            return true;
        } catch (TokenExpiredException e) {
            System.err.println("토큰이 만료되었습니다: " + e.getMessage());
        } catch (JWTVerificationException e) {
            System.err.println("토큰 검증 실패: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("예상치 못한 오류 발생: " + e.getMessage());
        }
        throw new TokenNotFoundException(ResponseCode.JWT_ACCESS_TOKEN_EXPIRED);
    }

    public Authentication getAuthentication(String accessToken) {
        String email = extractEmail(accessToken)
                .orElseThrow(() -> new TokenNotFoundException(ResponseCode.JWT_ACCESS_TOKEN_EXPIRED));

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ResponseCode.MEMBER_NOT_FOUND));

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
                throw new TokenNotFoundException(ResponseCode.UNKNOWN_TOKEN_TYPE);
            }
        } catch (JWTVerificationException e) {
            throw new TokenNotFoundException(ResponseCode.TOKEN_VALIDATION_FAILED);
        } catch (Exception e) {
            throw new TokenNotFoundException(ResponseCode.UNEXPECTED_ERROR);
        }
    }

    public TokenResponse refreshTokens(String refreshToken) {
        System.out.println("Received refreshToken: " + refreshToken);

        if (!isTokenValid(refreshToken)) {
            System.err.println("Invalid refreshToken");
            throw new TokenNotFoundException(ResponseCode.JWT_REFRESH_TOKEN_EXPIRED);
        }

        String email = extractEmail(refreshToken)
                .orElseThrow(() -> new TokenNotFoundException(ResponseCode.JWT_REFRESH_TOKEN_EXPIRED));
        System.out.println("Extracted email: " + email);

        Member member = memberRepository.findByEmail(email)
                .orElseThrow(() -> new MemberNotFoundException(ResponseCode.MEMBER_NOT_FOUND));

        String newAccessToken = createAccessToken(email);
        String newRefreshToken = createRefreshToken(email);
        updateRefreshToken(email, newRefreshToken);

        return new TokenResponse(newAccessToken, newRefreshToken);
    }

    public String extractAccessTokenFromHeader(String authorizationHeader) {
        return Optional.ofNullable(authorizationHeader)
                .filter(header -> header.startsWith(BEARER))
                .map(header -> header.replace(BEARER, ""))
                .orElseThrow(() -> new IllegalArgumentException("Invalid Authorization header"));
    }
    public String extractRefreshTokenFromHeader(String authorizationHeader) {
        return Optional.ofNullable(authorizationHeader)
                .filter(header -> header.startsWith(BEARER))
                .map(header -> header.replace(BEARER, ""))
                .orElseThrow(() -> new IllegalArgumentException("Invalid Authorization header"));
    }
}
