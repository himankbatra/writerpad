package com.xebia.fs101.writerpad.services.security.jwt;

import com.xebia.fs101.writerpad.services.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class JwtTokenSuccessHandler extends SimpleUrlAuthenticationSuccessHandler {
    @Value("${jwt.expires_in}")
    private int expiresIn;
    @Value("${jwt.cookie}")
    private String tokenCookie;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication)
            throws IOException, ServletException {
        clearAuthenticationAttributes(request);
        CustomUserDetails user = (CustomUserDetails) authentication.getPrincipal();
        String jws = this.jwtTokenProvider.generateToken(user);
        // Create token auth Cookie
        Cookie authCookie = new Cookie(this.tokenCookie, (jws));
        authCookie.setHttpOnly(true);
        authCookie.setMaxAge(this.expiresIn);
        authCookie.setPath("/");
        // Add cookie to response
        response.addCookie(authCookie);
        response.sendRedirect("/");
    }
}
