package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
public class HomeController {

	// GetMapping(value = "/home")
	@RequestMapping(value = "/home")
	public String displayHomePage(Model model) {
		model.addAttribute("username", "John Doe");
		return "home";
	}

	// @GetMapping(value = "/v2/home")
	@RequestMapping(value = "/v2/home")
	public ModelAndView displayV2HomePage(Model model) {
		ModelAndView modelAndView = new ModelAndView("home");
		modelAndView.addObject("username", "John Doe");
		return modelAndView;
	}
}
