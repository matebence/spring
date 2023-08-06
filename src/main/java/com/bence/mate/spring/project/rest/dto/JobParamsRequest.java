package com.bence.mate.spring.project.rest.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class JobParamsRequest {
	
	@Getter
	@Setter
	private String paramKey;

	@Setter
	@Getter
	private String paramValue;
}
