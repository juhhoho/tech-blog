package com.blog.auth.jwt;

import com.blog.auth.dto.LocalUserDetails;
import com.blog.auth.dto.SocialUserDetails;
import com.blog.auth.dto.UserDTO;
import io.jsonwebtoken.ExpiredJwtException;
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
import java.io.PrintWriter;

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
        String accessToken = request.getHeader("access");
        
        //Authorization 헤더 검증
        if (accessToken == null) {
            System.out.println("token null");

            // 권한이 필요없는 요청도 있기 때문에 다음 필터로 넘김
            filterChain.doFilter(request, response);

            //조건이 해당되면 메소드 종료 (필수)
            return;
        }

        // 토큰 만료 여부 확인, 만료시 절대 다음 필터로 넘기지 않음
        // 프론트로부터 refresh token을 바탕으로 갱신하도록 함.
        try {
            jwtUtil.isExpired(accessToken);
        } catch (ExpiredJwtException e) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("access token expired");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }

        // 토큰이 access인지 확인 (발급시 페이로드에 명시)
        String category = jwtUtil.getCategory(accessToken);
        if (!category.equals("access")) {

            //response body
            PrintWriter writer = response.getWriter();
            writer.print("invalid access token");

            //response status code
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }


        //토큰에서 username과 role 획득
        String username = jwtUtil.getUsername(accessToken);
        String role = jwtUtil.getRole(accessToken);

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

