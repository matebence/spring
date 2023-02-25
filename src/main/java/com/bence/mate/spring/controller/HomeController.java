package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.validation.Errors;
import org.springframework.ui.Model;

import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@CrossOrigin(maxAge = 3600)
public class HomeController {

	@GetMapping("/home")
    @CrossOrigin("http://front-end.com")
	public String getHomePage(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse) {
		log.info("{}", httpServletRequest);
		log.info("{}", httpServletResponse);
		return "homePage";
	}

	@GetMapping("/welcome")
	public String getWelcomePage(Authentication authentication, HttpSession session) {
		log.info("{}", session);
		log.info("{}", authentication);
		return "welcomePage";
	}

	@GetMapping("/admin")
	public String getAdminPage(Errors errors, Model model) {
		log.info("{}", model);
		log.info("{}", errors);
		return "adminPage";
	}

	@GetMapping("/emp")
	public String getEmployeePage() {
		return "empPage";
	}

	@GetMapping("/mgr")
	public String getManagerPage() {
		return "mgrPage";
	}

	@GetMapping("/common")
	public String getCommonPage() {
		return "commonPage";
	}

	@GetMapping("/common/read")
	public String getCommonReadPage() {
		return "commonReadPage";
	}

	@GetMapping("/common/write")
	public String getCommonWritePage() {
		return "commonWritePage";
	}

	@GetMapping("/accessDenied")
	public String getAccessDeniedPage() {
		return "accessDeniedPage";
	}
	
	@GetMapping("/invalidSession")
	public String getInvalidSessionPage() {
		return "invalidSession";
	}
}