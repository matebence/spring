package com.bence.mate.spring.listener;

import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.bence.mate.spring.dto.CustomerRegisteredEvent;
import com.bence.mate.spring.dto.CustomerRemovedEvent;
import com.bence.mate.spring.dto.OrderCompletedEvent;
import com.bence.mate.spring.service.EmailService;

@Component
public class EmailListeners {

	@Autowired	
	private EmailService emailService;

	@EventListener
	public void onRegisterEvent(CustomerRegisteredEvent event) {
		emailService.sendRegisterEmail(event.getCustomer());
	}

	@EventListener
	public void onCustomerRemovedEvent(CustomerRemovedEvent event) {
		emailService.sendCustomerRemovedEmail(event.getCustomer());
	}

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void onOrderCompletedEvent(OrderCompletedEvent event) {
		emailService.sendOrderEmail(event.getOrder());
	}
}
