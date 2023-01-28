package com.bence.mate.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

import com.bence.mate.spring.service.EmailService;

@Configuration
@EnableAspectJAutoProxy
//The @SpringBootApplication annotation contains the @EnableAutoConfiguration annotation.
// This autoconfiguration is one of the attractions of Spring Boot and makes configuration simpler.
// The auto configuration uses @Conditional type annotations (like @ConditionalOnClass and @ConditionalOnProperty) 
// to scan the classpath and look for key classes that trigger the loading of 'modules' like AOP.
@ComponentScan(basePackages = { "com.bence.mate.spring" })
public class Application {

	public static void main(String[] args) {
		AnnotationConfigApplicationContext applicationContext = new AnnotationConfigApplicationContext();
		applicationContext.register(Application.class);
		applicationContext.refresh();

		EmailService emailService = applicationContext.getBean(EmailService.class);
		emailService.sendEmail("Test email has been send.");

		applicationContext.close();
	}
}
