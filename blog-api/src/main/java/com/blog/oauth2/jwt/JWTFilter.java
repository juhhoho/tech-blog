package com.blog.oauth2.jwt;

import com.blog.oauth2.dto.LocalUserDetails;
import com.blog.oauth2.dto.SocialUserDetails;
import com.blog.oauth2.dto.UserDTO;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
public class JWTFilter extends OncePerRequestFilter {

    private final JWTUtil jwtUtil;

    public JWTFilter(JWTUtil jwtUtil) {

        this.jwtUtil = jwtUtil;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        log.info("[JWTFilter - doFilterInternal]");

        //request에서 Authorization 헤더를 찾음
        String authorization= request.getHeader("Authorization");
        
        //Authorization 헤더 검증
        if (authorization == null || !authorization.startsWith("Bearer ")) {

            System.out.println("token null");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        String token = authorization.split(" ")[1];

        //토큰 소멸 시간 검증
        if (jwtUtil.isExpired(token)) {

            System.out.println("token expired");
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(token);
        String role = jwtUtil.getRole(token);

        //userDTO를 생성하여 값 set
        UserDTO userDTO = UserDTO.builder()
                .username(username)
                .role(role)
                .build();

        // social user - naver
        if (username.startsWith("naver")){
            //UserDetails에 회원 정보 객체 담기
            SocialUserDetails socialUserDetails = new SocialUserDetails(userDTO);

            //스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(socialUserDetails, null, socialUserDetails.getAuthorities());
            //세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("1" );
            filterChain.doFilter(request, response);
        }
        // local user
        else {
            // UserDetails에 회원 정보 객체 담기
            LocalUserDetails localUserDetails = new LocalUserDetails(userDTO);

            // 스프링 시큐리티 인증 토큰 생성
            Authentication authToken = new UsernamePasswordAuthenticationToken(
                    localUserDetails, null, localUserDetails.getAuthorities());

            // 세션에 사용자 등록
            SecurityContextHolder.getContext().setAuthentication(authToken);
            System.out.println("2" );
            // 다음 필터로 요청 전달
            filterChain.doFilter(request, response);
        }


    }
}

