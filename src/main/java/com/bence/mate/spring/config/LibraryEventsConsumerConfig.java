package com.bence.mate.spring.config;

import org.springframework.boot.autoconfigure.kafka.ConcurrentKafkaListenerContainerFactoryConfigurer;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.util.backoff.FixedBackOff;
import org.springframework.context.annotation.Bean;
import org.apache.kafka.common.TopicPartition;

import org.springframework.kafka.config.ConcurrentKafkaListenerContainerFactory;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.dao.RecoverableDataAccessException;
import org.springframework.kafka.listener.ContainerProperties;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@Configuration
public class LibraryEventsConsumerConfig {

    public static final String SUCCESS = "SUCCESS";
    public static final String RETRY = "RETRY";
    public static final String DEAD = "DEAD";

    @Autowired
    private KafkaTemplate kafkaTemplate;

    @Value("${topics.dlt:library-events.DLT}")
    private String deadLetterTopic;

    @Value("${topics.retry:library-events.RETRY}")
    private String retryTopic;

    public DeadLetterPublishingRecoverer publishingRecoverer() {
        return new DeadLetterPublishingRecoverer(kafkaTemplate, (r, e) -> {
            log.error("Exception in publishingRecoverer : {} ", e.getMessage(), e);
            if (e.getCause() instanceof RecoverableDataAccessException) {
                return new TopicPartition(retryTopic, r.partition());
            } else {
                return new TopicPartition(deadLetterTopic, r.partition());
            }
        });
    }

    public DefaultErrorHandler errorHandler() {
        var exceptiopnToIgnorelist = List.of(IllegalArgumentException.class);

        ExponentialBackOffWithMaxRetries expBackOff = new ExponentialBackOffWithMaxRetries(2);
        expBackOff.setInitialInterval(1_000L);
        expBackOff.setMaxInterval(2_000L);
        expBackOff.setMultiplier(2.0);

        var fixedBackOff = new FixedBackOff(1000L, 2L);

        var defaultErrorHandler = new DefaultErrorHandler(
                publishingRecoverer(),
                fixedBackOff
                //expBackOff
        );

        exceptiopnToIgnorelist.forEach(defaultErrorHandler::addNotRetryableExceptions);
        defaultErrorHandler.setRetryListeners((record, ex, deliveryAttempt) -> log.info("Failed Record in Retry Listener  exception : {} , deliveryAttempt : {} ", ex.getMessage(), deliveryAttempt));

        return defaultErrorHandler;
    }

    @Bean
    @ConditionalOnMissingBean(name = "kafkaListenerContainerFactory")
    public ConcurrentKafkaListenerContainerFactory<?, ?> kafkaListenerContainerFactory(ConcurrentKafkaListenerContainerFactoryConfigurer configurer, ObjectProvider<ConsumerFactory<Object, Object>> kafkaConsumerFactory) {
        ConcurrentKafkaListenerContainerFactory<Object, Object> factory = new ConcurrentKafkaListenerContainerFactory<>();
        configurer.configure(factory, kafkaConsumerFactory.getIfAvailable());
        factory.getContainerProperties().setAckMode(ContainerProperties.AckMode.MANUAL);

        // then the first threads pulls the first partition
        // then the second threads pulls the second partition
        // then the third threads pulls the third partition
        // factory.setConcurrency(3);

        // Advanced way
        factory.setCommonErrorHandler(errorHandler());

        return factory;
    }
}
