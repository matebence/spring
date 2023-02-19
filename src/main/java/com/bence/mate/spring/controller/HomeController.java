package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.stereotype.Controller;

@Controller
public class HomeController {

	@GetMapping("/home")
	public String getHomePage() {
		return "homePage";
	}

	@GetMapping("/welcome")
	public String getWelcomePage() {
		return "welcomePage";
	}

	@GetMapping("/admin")
	public String getAdminPage() {
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
}