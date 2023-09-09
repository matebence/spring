package com.bence.mate.spring.service.consumer;

import org.springframework.kafka.listener.AcknowledgingMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import com.bence.mate.spring.service.LibraryEventsService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.kafka.annotation.KafkaListener;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;
import java.util.Optional;
@Slf4j
@Component
public class LibraryEventsConsumerManualOffset  implements AcknowledgingMessageListener<Integer,String> {
    @Autowired
    private LibraryEventsService libraryEventsService;

    @Override
    @KafkaListener(topics = {"library-events"})
    public void onMessage(ConsumerRecord<Integer, String> consumerRecord, Acknowledgment acknowledgment) {
        log.info("ConsumerRecord in Manual Offset Consumer: {} ", consumerRecord);

        try {
            libraryEventsService.processLibraryEvent(consumerRecord);
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        Optional.ofNullable(acknowledgment).ifPresent(Acknowledgment::acknowledge);
    }
}
