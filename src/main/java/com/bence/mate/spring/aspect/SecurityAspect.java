package com.bence.mate.spring.aspect;

import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.JoinPoint;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Aspect
@Order(2)
@Component
public class SecurityAspect {

	private static Logger logger = LoggerFactory.getLogger(SecurityAspect.class);

		@Pointcut(value = "within(com.bence.mate.spring.service.*)")
	//	@Pointcut(value = "execution(public char[] com.bence.mate.spring.service.EmailService.sendEmail(String))")  
	//	@Pointcut(value = "execution(public char[] com.bence.mate.spring.service.EmailService.*(String))")  
	//	@Pointcut(value = "execution(public char[] com.bence.mate.spring.service.EmailService.*(..))")  
	//	@Pointcut(value = "execution(public * com.bence.mate.spring.service.EmailService.*(..))")  
	//	@Pointcut(value = "execution(* com.bence.mate.spring.service.EmailService.*(..))")  
	public void securityPointCut() {
	}

	@Before(value = "securityPointCut()")
	public void beforeMethodCall(JoinPoint joinPoint) {
		logger.info("{}", joinPoint.getArgs()[0].toString());
	}

	@After(value = "securityPointCut() && args(message)", argNames = "message")
	public void afterMethodCall(JoinPoint joinPoint, String message) {
		logger.info("{}", message);
		logger.info("{}", joinPoint.getSignature().getName());
	}

	@AfterReturning(value = "securityPointCut()", returning = "result")
	public void afterReturningMethodCall(char[] result) {
		logger.info("{}", result);
	}

	@AfterThrowing(value = "securityPointCut()")
	public void afterThrowingMethodCall(JoinPoint joinPoint) {
		logger.info("{}", joinPoint.getSignature().getName());
	}

	@Around(value = "securityPointCut() && args(message)", argNames = "message")
	public void aroundMethodCall(ProceedingJoinPoint joinPoint, String message) throws Throwable {
		joinPoint.proceed();
		if (message.length() < 3) {
			throw new IllegalStateException("The message is empty");
		}
	}
}
