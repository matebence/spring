package com.bence.mate.spring.controller;

import com.bence.mate.spring.service.ApplicationScopedCounterService;
import com.bence.mate.spring.service.RequestScopedCounterService;
import com.bence.mate.spring.service.SessionScopedCounterService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;

@Controller
@RequestMapping(value = "/counter")
public class CounterController {

	@Autowired
	private ApplicationScopedCounterService applicationScopedCounterService;

	@Autowired
	private RequestScopedCounterService requestScopedCounterService;

	@Autowired
	private SessionScopedCounterService sessionScopedCounterService;

	// GetMapping(value = "/application")
	@RequestMapping(value = "/application")
	public String applicationScopedCounter(Model model) {
		applicationScopedCounterService.setCounter(applicationScopedCounterService.getCounter() + 1);
		model.addAttribute("counter", applicationScopedCounterService.getCounter());
		return "counter";
	}

	// GetMapping(value = "/request")
	@RequestMapping(value = "/request")
	public String requestScopedCounter(Model model) {
		requestScopedCounterService.setCounter(requestScopedCounterService.getCounter() + 1);
		model.addAttribute("counter", requestScopedCounterService.getCounter());
		return "counter";
	}

	// GetMapping(value = "/session")
	@RequestMapping(value = "/session")
	public String sessionScopedCounter(Model model) {
		sessionScopedCounterService.setCounter(sessionScopedCounterService.getCounter() + 1);
		model.addAttribute("counter", sessionScopedCounterService.getCounter());
		return "counter";
	}
}
