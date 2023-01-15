package com.bence.mate.spring.coupling.spring;

import org.springframework.stereotype.Component;

@Component
public class SumOperation implements MathOperation {

	@Override
	public double calculate(double a, double b) {
		return a + b;
	}
}
