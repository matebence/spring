package com.bence.mate.spring.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorMessage {

	@Getter
	@Setter
	private String message;
	
	private LocalDateTime localDateTime = LocalDateTime.now();
}
