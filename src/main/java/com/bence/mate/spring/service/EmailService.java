package com.bence.mate.spring.service;

import com.bence.mate.spring.aspect.annotation.Log;

import org.springframework.stereotype.Service;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

@Service
public class EmailService {

	private static Logger logger = LoggerFactory.getLogger(EmailService.class);

	@Log
	public char[] sendEmail(String message) {
		logger.info("{}", message);
		return message.toCharArray();
	}
}
