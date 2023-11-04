package com.bence.mate.auth.provider;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetailsService;
import com.bence.mate.auth.principal.AdditionalAuthenticationDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.bence.mate.auth.principal.SpringUser;

@Component
public class AdditionalAuthenticationProvider extends DaoAuthenticationProvider {

    @Override
    protected void additionalAuthenticationChecks(UserDetails userDetails, UsernamePasswordAuthenticationToken authentication) throws AuthenticationException {
        super.additionalAuthenticationChecks(userDetails, authentication);
        AdditionalAuthenticationDetails details = (AdditionalAuthenticationDetails) authentication.getDetails();
        SpringUser user = (SpringUser) userDetails;
        if(!getPasswordEncoder().matches(details.getSecurityPin(), user.getSecurityPin())) {
            throw new BadCredentialsException("Invalid security pin");
        }
        user.setSecurityPin(null);
    }

    @Override
    @Autowired
    public void setUserDetailsService(UserDetailsService userDetailsService) {
        super.setUserDetailsService(userDetailsService);
    }

    @Override
    @Autowired
    public void setPasswordEncoder(PasswordEncoder passwordEncoder) {
        super.setPasswordEncoder(passwordEncoder);
    }
}
