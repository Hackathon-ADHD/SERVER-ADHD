package adhd.diary.global.config;

import adhd.diary.auth.handler.JwtAuthenticationProcessingFilter;
import adhd.diary.auth.handler.OAuth2LoginFailureHandler;
import adhd.diary.auth.handler.OAuth2LoginSuccessHandler;
import adhd.diary.auth.jwt.JwtService;
import adhd.diary.auth.service.CustomOAuth2UserService;
import adhd.diary.member.domain.MemberRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final JwtService jwtService;
    private final MemberRepository memberRepository;
    private final OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler;
    private final OAuth2LoginFailureHandler oAuth2LoginFailureHandler;
    private final CustomOAuth2UserService customOAuth2UserService;

    public SecurityConfig(JwtService jwtService,
                          MemberRepository memberRepository,
                          OAuth2LoginSuccessHandler oAuth2LoginSuccessHandler,
                          OAuth2LoginFailureHandler oAuth2LoginFailureHandler,
                          CustomOAuth2UserService customOAuth2UserService) {
        this.jwtService = jwtService;
        this.memberRepository = memberRepository;
        this.oAuth2LoginSuccessHandler = oAuth2LoginSuccessHandler;
        this.oAuth2LoginFailureHandler = oAuth2LoginFailureHandler;
        this.customOAuth2UserService = customOAuth2UserService;
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
                .authorizeHttpRequests(authorizationRequest ->
                        authorizationRequest.requestMatchers(
                                AntPathRequestMatcher.antMatcher("/login/**")
                        ).permitAll()
                ).authorizeHttpRequests(authorizationRequest ->
                        authorizationRequest.requestMatchers(
                                AntPathRequestMatcher.antMatcher("/login/oauth2/code/signup"),
                                AntPathRequestMatcher.antMatcher("/api/update-nickname"),
                                AntPathRequestMatcher.antMatcher("/signup/nickname"),
                                AntPathRequestMatcher.antMatcher("/signup/birthday"),
                                AntPathRequestMatcher.antMatcher("/login/complete-registration"),
                                AntPathRequestMatcher.antMatcher("/")
                        ).authenticated().anyRequest().permitAll()
                ).oauth2Login(
                        oAuth2LoginConfigurer ->
                                oAuth2LoginConfigurer
                                        .successHandler(oAuth2LoginSuccessHandler)
                                        .failureHandler(oAuth2LoginFailureHandler)
                                        .userInfoEndpoint(userInfoEndpointConfig ->
                                                userInfoEndpointConfig.userService(customOAuth2UserService))
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
        JwtAuthenticationProcessingFilter jwtAuthenticationFilter = new JwtAuthenticationProcessingFilter(jwtService, memberRepository);
        return jwtAuthenticationFilter;
    }
}