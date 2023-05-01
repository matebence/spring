package com.bence.mate.spring.sort;

import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.Test;

import com.bence.mate.spring.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;

// @SpringBootTest is meta annotated with @ExtendWith(SpringExtension.class) and which means every time your tests are extended with SpringExtension
@ExtendWith(SpringExtension.class)
// only needed when main class is not in default location
@ContextConfiguration(classes = Application.class) 
// when we use XML based configuration
// @ContextConfiguration(locations = "/applicationContext.xml") 
public class BinarySearchTest {

	@Autowired
	private BinarySearchImpl binarySearch;

	@Test
	public void whenBinarySearchIsCalled_thenThreeIsExpected() {
		// given 
		// when
		int actualResult = binarySearch.binarySearch(new int[] {}, 5);

		// then
		assertEquals(3, actualResult);
	}
}