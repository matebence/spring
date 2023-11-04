package com.bence.mate.auth.principal;

import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.security.web.authentication.WebAuthenticationDetails;

import jakarta.servlet.http.HttpServletRequest;

public class AdditionalAuthenticationDetailsSource extends WebAuthenticationDetailsSource{

    @Override
    public WebAuthenticationDetails buildDetails(HttpServletRequest context) {
        return new AdditionalAuthenticationDetails(context);
    }
}
