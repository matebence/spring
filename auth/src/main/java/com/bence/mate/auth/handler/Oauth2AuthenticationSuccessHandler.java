package com.bence.mate.auth.handler;

import com.bence.mate.auth.principal.CustomAuthenticationPrincipal;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.ServletException;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;

@Slf4j
@Configuration
public class Oauth2AuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        CustomAuthenticationPrincipal principal = (CustomAuthenticationPrincipal) authentication.getPrincipal();
        log.info("{}", principal);
    }
}
