package com.bence.mate.auth.config;

import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.authority.mapping.GrantedAuthoritiesMapper;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.core.user.OAuth2UserAuthority;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.oauth2.core.oidc.OidcUserInfo;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.core.GrantedAuthority;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import java.util.stream.Collectors;
import java.util.*;

@Configuration
public class SecurityConfig {

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain configure(HttpSecurity http) throws Exception {
        return http.oauth2Client()
                .and().oauth2Login()
                .and().csrf().disable().sessionManagement().sessionCreationPolicy(SessionCreationPolicy.ALWAYS)
                .and().authorizeHttpRequests()
                .requestMatchers("/", "/home", "/access-denied").permitAll()
                .requestMatchers("/sign-in").authenticated()
                .requestMatchers("/manage").hasRole("USER")
                .and().exceptionHandling().accessDeniedPage("/access-denied")
                .and().logout().logoutSuccessUrl("http://localhost:8080/realms/spring/protocol/openid-connect/logout")
                .deleteCookies("JSESSIONID").invalidateHttpSession(true)
                .and().build();
    }

    @Bean
    public GrantedAuthoritiesMapper userAuthoritiesMapperForKeycloak() {
        return authorities -> {
            Set<GrantedAuthority> mappedAuthorities = new HashSet<>();
            GrantedAuthority authority = authorities.iterator().next();

            if (authority instanceof OidcUserAuthority) {
                OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                OidcUserInfo userInfo = oidcUserAuthority.getUserInfo();

                if (userInfo.hasClaim("realm_access")) {
                    Map<String, Object> realmAccess = userInfo.getClaimAsMap("realm_access");
                    Collection<GrantedAuthority> generateAuthoritiesFromClaim = ((Collection<String>) realmAccess.get("roles")).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim);
                }
            } else {
                OAuth2UserAuthority oauth2UserAuthority = (OAuth2UserAuthority) authority;
                Map<String, Object> userAttributes = oauth2UserAuthority.getAttributes();

                if (userAttributes.containsKey("realm_access")) {
                    Map<String,Object> realmAccess =  (Map<String,Object>) userAttributes.get("realm_access");
                    Collection<GrantedAuthority> generateAuthoritiesFromClaim = ((Collection<String>) realmAccess.get("roles")).stream()
                            .map(SimpleGrantedAuthority::new)
                            .collect(Collectors.toList());
                    mappedAuthorities.addAll(generateAuthoritiesFromClaim);
                }
            }
            return mappedAuthorities;
        };
    }
}
