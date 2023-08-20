package com.project.easysign.controller;

import com.project.easysign.dto.AuthorizationDTO;
import com.project.easysign.exception.AuthenticationFailedException;
import com.project.easysign.jwt.JwtProvider;
import com.project.easysign.security.UserPrincipal;
import com.project.easysign.service.UserService;
import com.project.easysign.util.CookieUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RequiredArgsConstructor
@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    private final UserService userService;
    private final AuthenticationManager authenticationManager;
    private final JwtProvider jwtProvider;

    @PostMapping("login")
    public ResponseEntity login(@RequestBody AuthorizationDTO authorizationRequest, HttpServletRequest request, HttpServletResponse response){
        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorizationRequest.getUsername(), authorizationRequest.getPassword()));
            UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
            String status = generateTokenCookie(userPrincipal, request, response);
            return ResponseEntity.status(HttpStatus.OK).body(status);
        }catch (AuthenticationException e){
            throw new AuthenticationFailedException("아이디 또는 비밀번호가 틀렸습니다.");
        }
    }
    private String generateTokenCookie(UserPrincipal userPrincipal, HttpServletRequest request, HttpServletResponse response){
        final int cookieMaxAge = jwtProvider.getTokenExpirationDate().intValue();
        // https 프로토콜인 경우 secure 옵션 사용
        boolean secure = request.isSecure();
        CookieUtils.addCookie(response, "access_token", jwtProvider.generateToken(userPrincipal.getUsername()), false, secure, cookieMaxAge);
        return "SUCCESS";
    }

    @PostMapping("/logout")
    public ResponseEntity expiredToken(HttpServletRequest request, HttpServletResponse response){
        CookieUtils.deleteCookie(request, response, "access_token");
        return ResponseEntity.ok("SUCCESS");
    }
}
