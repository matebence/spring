package com.bence.mate.auth.principal;

public interface CustomAuthenticationPrincipal {

    String getFirstName();

    String getLastName();

    String getFirstAndLastName();

    String getEmail();
}
