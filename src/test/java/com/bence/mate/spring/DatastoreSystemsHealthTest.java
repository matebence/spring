package com.bence.mate.spring;

import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import javax.sql.DataSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class DatastoreSystemsHealthTest {

	@Autowired
	private DataSource dataSource;

	@Test
	public void dbPrimaryIsOk() {
		try {
			// given
			// when
			String catalogName = dataSource.getConnection().getCatalog();

			// then
			assertEquals(catalogName, "spring");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
