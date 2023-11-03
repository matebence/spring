package com.bence.mate.auth.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;
import jakarta.servlet.http.HttpServletRequest;

import org.springframework.web.servlet.view.RedirectView;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class DefaultController {

    @GetMapping(value = {"/", "/home"})
    public ModelAndView home() {
        return new ModelAndView("home");
    }

    @GetMapping("/sign-in")
    public RedirectView signIn() {
        return new RedirectView("/");
    }

    @GetMapping("/manage")
    public ModelAndView manage(HttpServletRequest request) {
        return new ModelAndView("manage");
    }

    @GetMapping("/access-denied")
    public ModelAndView denied() {
        return new ModelAndView("access-denied");
    }
}
