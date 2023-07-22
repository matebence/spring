package com.bence.mate.spring.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import com.bence.mate.spring.dto.CustomerRegisteredEvent;
import com.bence.mate.spring.service.AnalyticsService;

@Component
public class AnalyticsCustomerRegisteredListener {

	@Autowired
	private AnalyticsService analyticsService;

	@Async
	@EventListener
	public void onRegisterEvent(CustomerRegisteredEvent event) {
		analyticsService.registerNewCustomer(event.getCustomer());
	}
}
