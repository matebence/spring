package com.bence.mate.spring;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MySQLContainer;

// https://www.testcontainers.org/modules/databases/mysql/
public class AbstractContainerBaseTest {

	// for custom images we use GenericContainer
	public static final MySQLContainer MY_SQL_CONTAINER;

	static {
		MY_SQL_CONTAINER = new MySQLContainer("mysql:latest").withUsername("test").withPassword("test")
				.withDatabaseName("test");
		// because we use start we don't need the @TestContainers annotation on the class level
		MY_SQL_CONTAINER.start();

	}

	@DynamicPropertySource
	public static void dynamicPropertySource(DynamicPropertyRegistry registry) {
		registry.add("spring.datasource.url", MY_SQL_CONTAINER::getJdbcUrl);
		registry.add("spring.datasource.username", MY_SQL_CONTAINER::getUsername);
		registry.add("spring.datasource.password", MY_SQL_CONTAINER::getPassword);
	}
}
