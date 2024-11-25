package com.blog.auth.config;

import com.blog.auth.jwt.CustomLogoutFilter;
import com.blog.auth.jwt.JWTFilter;
import com.blog.auth.jwt.JWTUtil;
import com.blog.auth.jwt.SocialUserLoginHandler;
import com.blog.auth.repository.refresh.RefreshCustomRepository;
import com.blog.auth.repository.refresh.RefreshRepository;
import com.blog.auth.repository.user.BaseUserRepository;
import com.blog.auth.service.user.SocialUserService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final SocialUserService socialUserService;
    private final SocialUserLoginHandler socialUserLoginHandler;
    private final RefreshRepository refreshRepository;
    private final RefreshCustomRepository refreshCustomRepository;
    private final JWTUtil jwtUtil;
    private final BaseUserRepository baseUserRepository;

    public SecurityConfig(SocialUserService socialUserService, SocialUserLoginHandler socialUserLoginHandler, RefreshRepository refreshRepository, RefreshCustomRepository refreshCustomRepository, JWTUtil jwtUtil, BaseUserRepository baseUserRepository) {
        this.socialUserService = socialUserService;
        this.socialUserLoginHandler = socialUserLoginHandler;
        this.refreshRepository = refreshRepository;
        this.refreshCustomRepository = refreshCustomRepository;
        this.jwtUtil = jwtUtil;
        this.baseUserRepository = baseUserRepository;
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {

        //csrf disable
        http.csrf((auth) -> auth.disable());

        //Form 로그인 방식 disable
        http.formLogin((auth) -> auth.disable());

        //HTTP Basic 인증 방식 disable
        http.httpBasic((auth) -> auth.disable());

        // H2 콘솔 프레임 옵션 설정
        http
                .headers((headers) -> headers
                        .frameOptions((frameOptions) -> frameOptions.sameOrigin()));

        //세션 설정 : STATELESS
        http
                .sessionManagement((session) -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        //경로별 인가 작업
        http
                .authorizeHttpRequests((auth) -> auth
                        .requestMatchers("/admin/**").hasRole("ADMIN")
                        .requestMatchers("/h2-console/**", "/login", "/register", "/reissue").permitAll()
                        .anyRequest().authenticated());


        // JWT 필터 추가 (모든 요청에 대해 인증 처리)
        http.addFilterBefore(new JWTFilter(jwtUtil), UsernamePasswordAuthenticationFilter.class);


        // OAuth2 소셜 로그인 설정
        http.oauth2Login(oauth2 -> oauth2
                .userInfoEndpoint(userInfo -> userInfo.userService(socialUserService))
                .successHandler(socialUserLoginHandler)); // 소셜 로그인 성공 시 socialUserLoginHandler 호출

        http
                .addFilterBefore(new CustomLogoutFilter(jwtUtil, refreshRepository, refreshCustomRepository, baseUserRepository), LogoutFilter.class);



        return http.build();
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {

        return configuration.getAuthenticationManager();
    }

    // PasswordEncoder interface의 구현체가 BCryptPasswordEncoder임을 수동 빈 등록을 통해 명시
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

}