package com.bence.mate.spring.general.entity;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Error {
	
	@Getter
	@Setter
    private String message;

	private final LocalDateTime localDateTime = LocalDateTime.now();    
}
