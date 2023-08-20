package com.bence.mate.spring.service;

import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.core.MessagePostProcessor;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Service;

import com.bence.mate.spring.dto.BookOrder;

import jakarta.jms.JMSException;
import jakarta.jms.Message;

@Service
public class BookOrderService {

    private static final String BOOK_QUEUE = "book.order.queue";

    @Autowired
    private JmsTemplate jmsTemplate;

    @Transactional
    public void send(BookOrder bookOrder, String storeId, String orderState){
        jmsTemplate.convertAndSend(BOOK_QUEUE, bookOrder, new MessagePostProcessor() {
            @Override
            public Message postProcessMessage(Message message) throws JMSException {
                message.setStringProperty("bookOrderId", bookOrder.getBookOrderId());
                message.setStringProperty("storeId", storeId);
                message.setStringProperty("orderState", orderState);
                return message;
            }
        });
    }
}
