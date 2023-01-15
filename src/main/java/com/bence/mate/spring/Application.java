package com.bence.mate.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.context.annotation.ComponentScan;

import com.bence.mate.spring.coupling.spring.qualifier.SearchService;
import com.bence.mate.spring.coupling.spring.MathService;

import com.bence.mate.spring.coupling.vanilla.SortService;
import com.bence.mate.spring.coupling.vanilla.QuickSort;

import com.bence.mate.spring.xml.SpringApp;
import com.bence.mate.spring.xml.IndexApp;
import com.bence.mate.spring.xml.JavaApp;
import com.bence.mate.spring.xml.XmlApp;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@SpringBootApplication
@ComponentScan(value = "com.bence.mate.spring")
// If the classes MathService, SearchService would exists out of the package where 
// the Application class exists 'com.bence.mate.spring' we would have to define the
// @ComponentScan annotation
public class Application {

	private static Logger logger = LoggerFactory.getLogger(Application.class); 

	public static void main(String[] args) {			
		// Using Spring without SpringBoots - xml
		ClassPathXmlApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");
		
		SpringApp springApp = xmlApplicationContext.getBean(SpringApp.class);
		IndexApp indexApp = xmlApplicationContext.getBean(IndexApp.class);
		JavaApp javaApp = xmlApplicationContext.getBean(JavaApp.class);
		XmlApp xmlApp = xmlApplicationContext.getBean(XmlApp.class);

		logger.info("{}", indexApp.callTheService());
		logger.info("{}", springApp.callTheService());
		logger.info("{}", javaApp.callTheService());
		logger.info("{}", xmlApp.callTheService());
		
		xmlApplicationContext.close();
		
		// Using Spring without SpringBoots - annotation
		AnnotationConfigApplicationContext annotationApplicationContext = new AnnotationConfigApplicationContext();
		annotationApplicationContext.register(Application.class);
		annotationApplicationContext.refresh();
		
		SortService sortService = new SortService(new QuickSort());
		logger.info("{}", sortService.sort(new int[] {6,7,8}));
		
		MathService mathService = annotationApplicationContext.getBean(MathService.class);
		logger.info("{}", mathService.calculate(5.0d, 42.2d));
		
		SearchService searchService = annotationApplicationContext.getBean(SearchService.class);
		logger.info("{}", searchService.found(new int[] {1,2,3}, 3));
		
		MathService mathServicePrototype = annotationApplicationContext.getBean(MathService.class);
		SearchService searchServiceSingleton = annotationApplicationContext.getBean(SearchService.class);
		
		logger.info("{}", searchServiceSingleton.equals(searchService));
		logger.info("{}", mathServicePrototype.equals(mathService));
		
		annotationApplicationContext.close();
		
		// Using Spring with SpringBoots
		// ApplicationContext applicationContext = SpringApplication.run(Application.class, args);
	}
}
