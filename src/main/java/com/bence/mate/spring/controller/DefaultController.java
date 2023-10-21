package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.ServletException;

@Controller
public class DefaultController {

    @GetMapping(value = {"/", "/home"})
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @GetMapping("/login")
    public RedirectView login() {
        return new RedirectView("/");
    }

    @GetMapping("/logout")
    public RedirectView logout(HttpServletRequest httpServletRequest) throws ServletException {
        httpServletRequest.logout();
        return new RedirectView("/");
    }

    // @PreAuthorize("hasAuthority('ADMIN')")
    @GetMapping("/manage")
    public ModelAndView manage() {
        return new ModelAndView("manage");
    }

    @GetMapping("/access-denied")
    public ModelAndView denied() {
        return new ModelAndView("access-denied");
    }
}
