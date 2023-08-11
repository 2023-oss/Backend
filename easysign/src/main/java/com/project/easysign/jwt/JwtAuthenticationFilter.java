package com.project.easysign.jwt;

import com.project.easysign.security.UserDetailsServiceImpl;
import com.project.easysign.security.UserPrincipal;
import com.project.easysign.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final UserDetailsServiceImpl userDetailsService;
    private final JwtProvider jwtProvider;
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String username = null;
        String jwt = null;

        Optional<Cookie> jwtCookie = CookieUtils.getCookie(request, "access_token");

        if(jwtCookie.isPresent()){
            jwt = jwtCookie.get().getValue();
            username = jwtProvider.extractUsername(jwt);
        }
        if(username!=null && SecurityContextHolder.getContext().getAuthentication() == null){
            UserPrincipal userPrincipal = (UserPrincipal) userDetailsService.loadUserByUsername(username);

            // 토큰이 유효하다면
            if(jwtProvider.validateToken(jwt, userPrincipal.getUsername())){
                // 새로운 인증 정보를 생성
                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userPrincipal, null, userPrincipal.getAuthorities());
                usernamePasswordAuthenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                // 인증 정보를 SecurityContextHolder에 저장
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
