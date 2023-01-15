package com.bence.mate.spring.coupling.spring;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;

@Primary
@Component
public class SubstractOperation implements MathOperation {

	@Override
	public double calculate(double a, double b) {
		return a - b;
	}
}
