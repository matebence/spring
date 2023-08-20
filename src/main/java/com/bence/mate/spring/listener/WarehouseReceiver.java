package com.bence.mate.spring.listener;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

import com.bence.mate.spring.dto.ProcessedBookOrder;
import com.bence.mate.spring.dto.BookOrder;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class WarehouseReceiver {

	// We dont need this because we use @SendTo
    @Autowired
    private JmsTemplate jmsTemplate;
    
    @Autowired
    private WarehouseProcessingService warehouseProcessingService;
    
    // With SendTo the method definition is:
    /*@Transactional
    @SendTo("book.order.processed.queue")
    @JmsListener(destination = "book.order.queue")
    public JmsResponse<Message<ProcessedBookOrder>> receive(@Payload BookOrder bookOrder,
			            @Header(name = "orderState") String orderState,
			            @Header(name = "bookOrderId") String bookOrderId,
			            @Header(name = "storeId") String storeId,
			            MessageHeaders messageHeaders) {
    }*/
    
	// JSON
    // With dynamic routing
    @JmsListener(destination = "book.order.queue")
    public JmsResponse<Message<ProcessedBookOrder>> receive(@Payload BookOrder bookOrder,
			            @Header(name = "orderState") String orderState,
			            @Header(name = "bookOrderId") String bookOrderId,
			            @Header(name = "storeId") String storeId) {
    	
        log.info("Message received!");
        log.info("Message is {} ", bookOrder);
        log.info("Message property orderState = {}, bookOrderId = {}, storeId = {}", orderState, bookOrderId, storeId);

        // With this we make sure, that this message ends in the dead letter queue
        if(bookOrder.getBook().getTitle().startsWith("L")){
            throw new RuntimeException("bookOrderId=" + bookOrder.getBookOrderId() + " is of a book not allowed!"); //it will retry 7 times to send the message here because of transaction, but then it puts in the dead letter queue
        }
        
        // Because we use sendTo with return statement this is no needed anymore
        /*ProcessedBookOrder order = new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()

        );
        jmsTemplate.convertAndSend("book.order.processed.queue", order);*/
        
        return warehouseProcessingService.processOrder(bookOrder, orderState, storeId);
    }
    
    // XML
    /*@JmsListener(destination = "book.order.queue")
    public void receive(Message message) throws JMSException{          
        log.info("Message received!");
        log.info("Message is == {}", ((TextMessage) message).getText());
    }*/
}
