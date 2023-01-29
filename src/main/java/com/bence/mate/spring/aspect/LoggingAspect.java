package com.bence.mate.spring.aspect;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.JoinPoint;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Aspect
@Order(1)
@Component
public class LoggingAspect {

	private static Logger logger = LoggerFactory.getLogger(LoggingAspect.class);

	// if Aspect and annotation are in the same package
	// we don't have to prefix with package name
	// if we have only one condition we can skip @Pointcut and use @Before etc ...
	@Pointcut(value = "@annotation(com.bence.mate.spring.aspect.annotation.Log)")
	public void logPointCut() {
	}

	@Before(value = "logPointCut()")
	public void beforeMethodCall(JoinPoint joinPoint) {
		logger.info("{}", joinPoint.getArgs()[0].toString());
	}

	@After(value = "logPointCut() && args(message)", argNames = "message")
	public void afterMethodCall(JoinPoint joinPoint, String message) {
		logger.info("{}", message);
		logger.info("{}", joinPoint.getSignature().getName());
	}

	@AfterReturning(value = "logPointCut()", returning = "result")
	public void afterReturningMethodCall(char[] result) {
		logger.info("{}", result);
	}

	@AfterThrowing(value = "logPointCut()")
	public void afterThrowingMethodCall(JoinPoint joinPoint) {
		logger.info("{}", joinPoint.getSignature().getName());
	}

	@Around(value = "logPointCut() && args(message)", argNames = "message")
	public void aroundMethodCall(ProceedingJoinPoint joinPoint, String message) throws Throwable {
		joinPoint.proceed();
		if (message.length() < 3) {
			throw new IllegalStateException("The message is empty");
		}
	}
}
