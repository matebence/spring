package com.bence.mate.spring.entity;

import java.time.LocalDateTime;
import java.io.Serializable;

import lombok.RequiredArgsConstructor;
import lombok.Getter;

@RequiredArgsConstructor
public class Error implements Serializable {

	@Getter
	private final String reason;

	@Getter
	private LocalDateTime at = LocalDateTime.now();
}
