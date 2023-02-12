package com.bence.mate.spring.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@ControllerAdvice
public class GlobalExceptionController {

	// This can be customized further via ErrorControl and custom exceptions with HttpResponse Annotation
	@ExceptionHandler({ NullPointerException.class, ArrayIndexOutOfBoundsException.class })
	public ModelAndView exceptionHandler(Exception exception) {
		ModelAndView errorPage = new ModelAndView();
		errorPage.setViewName("error");
		log.error(exception.getMessage());
		errorPage.addObject("errormsg", exception.getMessage());
		return errorPage;
	}
}
