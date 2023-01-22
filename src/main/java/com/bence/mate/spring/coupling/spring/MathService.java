package com.bence.mate.spring.coupling.spring;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.context.annotation.Scope;

import org.springframework.stereotype.Component;

@Component
@Scope(value = BeanDefinition.SCOPE_PROTOTYPE, proxyMode = ScopedProxyMode.TARGET_CLASS)
// With proxyMode we make sure that where ever we inject this class it will be always a new instance (in ApplicationContext)
public class MathService {

	// Using field injection
	@Autowired
	private MathOperation mathOperation;

	/*
	 * Using constructor injection public MathService(MathOperation mathOperation) {
	 * this.mathOperation = mathOperation; }
	 */

	/*
	 * Using setter injection public void setMathOperation(MathOperation
	 * mathOperation) { this.mathOperation = mathOperation; }
	 */

	public double calculate(double a, double b) {
		return this.mathOperation.calculate(a, b);
	}
}
