package com.bence.mate.spring.listener;

import org.springframework.stereotype.Component;

import jakarta.jms.MessageListener;
import jakarta.jms.JMSException;
import jakarta.jms.TextMessage;
import jakarta.jms.Message;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class BookOrderProcessingMessageListener implements MessageListener {

    @Override
    public void onMessage(Message message) {
        try {
            String text = ((TextMessage) message).getText();
            log.info(text);
        } catch (JMSException e) {
            e.printStackTrace();
        }
    }
}