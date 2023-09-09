package com.bence.mate.spring.scheduler;

import com.bence.mate.spring.config.LibraryEventsConsumerConfig;
import com.bence.mate.spring.repository.FailureRecordRepository;
import com.bence.mate.spring.service.LibraryEventsService;
import com.bence.mate.spring.entity.FailureRecord;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class RetryScheduler {

    @Autowired
    private LibraryEventsService libraryEventsService;

    @Autowired
    private FailureRecordRepository failureRecordRepository;

    @Scheduled(fixedRate = 10000 )
    public void retryFailedRecords() {
        log.info("Retrying Failed Records Started!");
        var status = LibraryEventsConsumerConfig.RETRY;

        failureRecordRepository.findAllByStatus(status)
                .forEach(failureRecord -> {
                    try {
                        var consumerRecord = buildConsumerRecord(failureRecord);
                        libraryEventsService.processLibraryEvent(consumerRecord);
                        failureRecord.setStatus(LibraryEventsConsumerConfig.SUCCESS);
                    } catch (Exception e){
                        log.error("Exception in retryFailedRecords : ", e);
                    }
                });
    }

    private ConsumerRecord<Integer, String> buildConsumerRecord(FailureRecord failureRecord) {
        return new ConsumerRecord<>(failureRecord.getTopic(),
                failureRecord.getPartition(), failureRecord.getOffsetValue(), failureRecord.getKey(),
                failureRecord.getErrorRecord());
    }
}
