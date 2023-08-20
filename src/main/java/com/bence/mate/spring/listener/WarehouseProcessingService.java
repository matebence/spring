package com.bence.mate.spring.listener;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.jms.listener.adapter.JmsResponse;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.messaging.Message;

import com.bence.mate.spring.dto.ProcessedBookOrder;
import com.bence.mate.spring.dto.BookOrder;

import lombok.extern.slf4j.Slf4j;
import java.util.Date;

@Slf4j
@Service
public class WarehouseProcessingService {

    private static final String PROCESSED_QUEUE = "book.order.processed.queue";
    
    private static final String CANCELED_QUEUE = "book.order.canceled.queue";
    
	// We dont need this because we use @SendTo
    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    // Representing our headers data with the Message wrapper class
    public JmsResponse<Message<ProcessedBookOrder>> processOrder(BookOrder bookOrder, String orderState, String storeId) {
    	
        Message<ProcessedBookOrder> message;

        // Dynamic destination strategies via JmsResponse class
        if("NEW".equalsIgnoreCase(orderState)){
            message = add(bookOrder, storeId);
            return JmsResponse.forQueue(message, PROCESSED_QUEUE);
        } else if("UPDATE".equalsIgnoreCase(orderState)){
            message = update(bookOrder, storeId);
            return JmsResponse.forQueue(message, PROCESSED_QUEUE);
        } else if("DELETE".equalsIgnoreCase(orderState)){
            message = delete(bookOrder,storeId);
            return JmsResponse.forQueue(message, CANCELED_QUEUE);
        } else{
            throw new IllegalArgumentException("WarehouseProcessingService.processOrder(...) - orderState does not match expected criteria!");
        }
    }

    private Message<ProcessedBookOrder> add(BookOrder bookOrder, String storeId){
    	log.info("ADDING A NEW ORDER TO DB");
    	ProcessedBookOrder processedBookOrder = new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        );
    	
    	return MessageBuilder
	        .withPayload(processedBookOrder)
	        .setHeader("orderState", "ADD")
	        .setHeader("storeId", storeId)
	        .build();
    }
    private Message<ProcessedBookOrder> update(BookOrder bookOrder, String storeId){
    	log.info("UPDATING A ORDER TO DB");
    	ProcessedBookOrder processedBookOrder = new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        );
    	
    	return MessageBuilder
	        .withPayload(processedBookOrder)
	        .setHeader("orderState", "UPDATE")
	        .setHeader("storeId", storeId)
	        .build();
    }
    private Message<ProcessedBookOrder> delete(BookOrder bookOrder, String storeId){
        log.info("DELETING ORDER FROM DB");
    	ProcessedBookOrder processedBookOrder = new ProcessedBookOrder(
                bookOrder,
                new Date(),
                new Date()
        );
    	
    	return MessageBuilder
	        .withPayload(processedBookOrder)
	        .setHeader("orderState", "DELETE")
	        .setHeader("storeId", storeId)
	        .build();
    }
}
