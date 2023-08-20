package com.bence.mate.spring.config;

import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.PlatformTransactionManager;

import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerEndpointRegistrar;
import org.springframework.jms.connection.CachingConnectionFactory;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.annotation.JmsListenerConfigurer;
import org.springframework.jms.config.SimpleJmsListenerEndpoint;
import org.springframework.jms.connection.JmsTransactionManager;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.support.converter.MessageType;

import org.springframework.context.annotation.Configuration;
import org.springframework.beans.factory.annotation.Value;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.springframework.context.annotation.Bean;

import com.bence.mate.spring.listener.BookOrderProcessingMessageListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
@EnableTransactionManagement
public class JmsConfig implements JmsListenerConfigurer {

    @Value("${spring.activemq.user}")
    private String user;

    @Value("${spring.activemq.password}")
    private String password;
    
    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Bean
    public MessageConverter jacksonJmsMessageConverter() {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_type");
        
        return converter;
    }

    @Bean
    public CachingConnectionFactory connectionFactory() {
        CachingConnectionFactory factory = new CachingConnectionFactory(new ActiveMQConnectionFactory(user, password, brokerUrl));
        factory.setClientId("StoreFront");
        factory.setSessionCacheSize(100);
        
        return factory;
    }

    @Bean
    public DefaultJmsListenerContainerFactory jmsListenerContainerFactory() {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory());
        factory.setMessageConverter(jacksonJmsMessageConverter());
        factory.setTransactionManager(jmsTransactionManager());
        factory.setErrorHandler(t -> log.info("Handling error in listening for messages, error: " + t.getMessage()));
        
        return factory;
    }

    @Bean
    public PlatformTransactionManager jmsTransactionManager(){
        return new JmsTransactionManager(connectionFactory());
    }
    
    @Bean
    public BookOrderProcessingMessageListener jmsMessageListener(){
        return new BookOrderProcessingMessageListener();
    }

    @Override
    public void configureJmsListeners(JmsListenerEndpointRegistrar registrar) {
        SimpleJmsListenerEndpoint endpoint = new SimpleJmsListenerEndpoint();
        endpoint.setMessageListener(jmsMessageListener());
        endpoint.setDestination("book.order.processed.queue");
        endpoint.setId("book-order-processed-queue");
        endpoint.setConcurrency("1");
        endpoint.setSubscription("book-order-processed-queue-subscription");
        registrar.registerEndpoint(endpoint, jmsListenerContainerFactory());
        registrar.setContainerFactory(jmsListenerContainerFactory());
    }
    
    // Dead letter queue config
    @Bean
    public JmsTemplate jmsTemplate(){
        JmsTemplate jmsTemplate = new JmsTemplate(connectionFactory());
        jmsTemplate.setMessageConverter(jacksonJmsMessageConverter());
        jmsTemplate.setDeliveryPersistent(true);
        jmsTemplate.setSessionTransacted(true);
        return jmsTemplate;
    }
    
    // XML
	/*@Bean
    public MessageConverter xmlMarshallingMessageConverter(){
        MarshallingMessageConverter converter = new MarshallingMessageConverter(xmlMarshaller());
        converter.setTargetType(MessageType.TEXT);
        return converter;
    }
    
    @Bean
    public XStreamMarshaller xmlMarshaller(){
        XStreamMarshaller marshaller =  new XStreamMarshaller();
        marshaller.setSupportedClasses(Book.class, Customer.class, BookOrder.class);
        return marshaller;
    }*/
    
    // Simple ConnectionFactory, we used cached instead
    /* @Bean
	public ActiveMQConnectionFactory connectionFactory() {
		return new ActiveMQConnectionFactory("admin", "admin", "tcp://localhost:61616");
	}*/
}
