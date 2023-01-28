package com.bence.mate.spring;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ComponentScan;

import com.bence.mate.spring.properties.PropertiesWithJavaConfig;

import com.bence.mate.spring.coupling.spring.qualifier.SearchService;
import com.bence.mate.spring.coupling.spring.MathService;

import com.bence.mate.spring.coupling.vanilla.SortService;
import com.bence.mate.spring.coupling.vanilla.QuickSort;

import com.bence.mate.spring.bean.VehicleService;
import com.bence.mate.spring.bean.Vehicle;

import java.util.function.Supplier;
import java.util.Random;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Configuration
@ComponentScan(basePackages = { "com.bence.mate.spring" })
public class AnnotationApplication {

	private static Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		// Using Spring without SpringBoots - annotation
		AnnotationConfigApplicationContext annotationApplicationContext = new AnnotationConfigApplicationContext();
		annotationApplicationContext.register(Application.class);
		annotationApplicationContext.refresh();

		SortService sortService = new SortService(new QuickSort());
		logger.info("{}", sortService.sort(new int[] { 6, 7, 8 }));

		MathService mathService = annotationApplicationContext.getBean(MathService.class);
		logger.info("{}", mathService.calculate(5.0d, 42.2d));

		SearchService searchService = annotationApplicationContext.getBean(SearchService.class);
		logger.info("{}", searchService.found(new int[] { 1, 2, 3 }, 3));

		MathService mathServicePrototype = annotationApplicationContext.getBean(MathService.class);
		SearchService searchServiceSingleton = annotationApplicationContext.getBean(SearchService.class);

		PropertiesWithJavaConfig propertiesWithJavaConfig = annotationApplicationContext
				.getBean(PropertiesWithJavaConfig.class);

		Vehicle BMW = annotationApplicationContext.getBean("BMW", Vehicle.class);
		Vehicle vehicle = annotationApplicationContext.getBean("vehicle", Vehicle.class);
//		Vehicle vehicle = annotationApplicationContext.getBean(Vehicle.class);
		Integer year = annotationApplicationContext.getBean(Integer.class);
		String hello = annotationApplicationContext.getBean(String.class);
		VehicleService vehcService = annotationApplicationContext.getBean(VehicleService.class);

		Supplier<Vehicle> audiSupplier = () -> {
			Vehicle audi = new Vehicle();
			audi.setName("Audi");
			return audi;
		};

		Random random = new Random();
		int randomNumber = random.nextInt(10);

		if ((randomNumber % 2) == 0) {
			annotationApplicationContext.registerBean("audi", Vehicle.class, audiSupplier);
		}

		try {
			Vehicle audi = annotationApplicationContext.getBean("Audi", Vehicle.class);
			logger.info("{}", audi.getName());
		} catch (NoSuchBeanDefinitionException noSuchBeanDefinitionException) {
			System.out.println("Error while creating Audi vehicle");
		}

		logger.info("{}", BMW.getName());
		logger.info("{}", vehicle.getName());
		logger.info("{}", year);
		logger.info("{}", hello);

		logger.info("{}", searchServiceSingleton.equals(searchService));
		logger.info("{}", mathServicePrototype.equals(mathService));

		annotationApplicationContext.close();
	}
}
