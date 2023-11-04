package com.bence.mate.auth.principal;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;

import lombok.Getter;
import lombok.Setter;

public class SpringUser extends User implements CustomAuthenticationPrincipal {

    public SpringUser(String username, String password, boolean enabled, boolean accountNonExpired, boolean credentialsNonExpired, boolean accountNonLocked, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
    }

    @Setter
    @Getter
    private String securityPin;

    @Setter
    @Getter
    private boolean otpEnabled;

    @Setter
    @Getter
    private String firstName;

    @Setter
    @Getter
    private String lastName;

    @Setter
    @Getter
    private String email;

    @Override
    public String getFirstAndLastName() {
        return firstName.concat(" ").concat(lastName);
    }
}
