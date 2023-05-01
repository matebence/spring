package com.bence.mate.spring.sort.cdi;

import org.springframework.test.context.ContextConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.Mock;

import com.bence.mate.spring.Application;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
@ContextConfiguration(classes = Application.class)
public class SomeCdiBusinessTest {

	@InjectMocks
	private SomeCdiBusiness business;

	@Mock
	private SomeCdiDao daoMock;

	@Test
	public void whenArrayIsSet_thenGreaterIsExpected() {
		// given
		// when
		Mockito.when(daoMock.getData()).thenReturn(new int[] { 2, 4 });

		// then
		assertEquals(4, business.findGreatest());
	}

	@Test
	public void whenThereAreNoElements_thenMinValueIsExpected() {
		// given
		// when
		Mockito.when(daoMock.getData()).thenReturn(new int[] {});

		// then
		assertEquals(Integer.MIN_VALUE, business.findGreatest());
	}

	@Test
	public void whenArrayWithEqualElementsIsSet_thenGreaterIsExpected() {
		// given
		// when
		Mockito.when(daoMock.getData()).thenReturn(new int[] { 2, 2 });

		// then
		assertEquals(2, business.findGreatest());
	}
}
