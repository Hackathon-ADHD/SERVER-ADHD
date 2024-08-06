package adhd.diary.global.config;

import adhd.diary.auth.handler.JwtAuthenticationProcessingFilter;
import adhd.diary.auth.jwt.JwtBlacklistService;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.member.domain.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final RedisTemplate<String, Object> redisTemplate;

    private final JwtBlacklistService jwtBlacklistService;

    public SecurityConfig(JwtService jwtService,
                          MemberRepository memberRepository,
                          RedisTemplate<String, Object> redisTemplate,
                          JwtBlacklistService jwtBlacklistService) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
        this.redisTemplate = redisTemplate;
        this.jwtBlacklistService = jwtBlacklistService;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(csrf -> csrf.disable())
                .formLogin(formLogin -> formLogin.disable())
                .httpBasic(httpBasic -> httpBasic.disable())
                .headers(headersConfigurer ->
                        headersConfigurer.frameOptions(
                                frameOptionsConfig ->
                                        frameOptionsConfig.disable()
                ))
                .sessionManagement(sessionManagement ->
                        sessionManagement.sessionCreationPolicy(
                            SessionCreationPolicy.STATELESS
                ))
                .exceptionHandling(httpSecurityExceptionHandlingConfigurer ->
                        httpSecurityExceptionHandlingConfigurer.authenticationEntryPoint(
                                new HttpStatusEntryPoint(HttpStatus.NOT_FOUND)
                        ))
                .authorizeHttpRequests(authorizationRequest ->
                        authorizationRequest.requestMatchers(
                                AntPathRequestMatcher.antMatcher("/favicon.ico"),
                                AntPathRequestMatcher.antMatcher("/swagger-ui-diary.html")
                        ).permitAll()
                )
                .authorizeHttpRequests(authorizationRequest ->
                        authorizationRequest.requestMatchers(
                                AntPathRequestMatcher.antMatcher("/login"),
                                AntPathRequestMatcher.antMatcher("/api/**")
                        ).permitAll()
                ).authorizeHttpRequests(authorizationRequest ->
                        authorizationRequest.requestMatchers(
                                AntPathRequestMatcher.antMatcher("/login/oauth2/code/signup"),
                                AntPathRequestMatcher.antMatcher("/api/update-nickname"),
                                AntPathRequestMatcher.antMatcher("/signup/nickname"),
                                AntPathRequestMatcher.antMatcher("/signup/birthday"),
                                AntPathRequestMatcher.antMatcher("/login/complete-registration"),
                                AntPathRequestMatcher.antMatcher("/"),
                                AntPathRequestMatcher.antMatcher("/api/**")
                        ).authenticated().anyRequest().permitAll()
                )
                .addFilterBefore(jwtAuthenticationProcessingFilter(), UsernamePasswordAuthenticationFilter.class)
                .addFilterAfter(new UsernamePasswordAuthenticationFilter(), LogoutFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity.getSharedObject(AuthenticationManager.class);
    }

    @Bean
    public JwtAuthenticationProcessingFilter jwtAuthenticationProcessingFilter() {
        return new JwtAuthenticationProcessingFilter(jwtService, memberRepository, redisTemplate, jwtBlacklistService);
    }
}