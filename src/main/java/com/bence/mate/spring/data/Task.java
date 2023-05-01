package com.bence.mate.spring.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Task {

	@Getter
	@Setter
	private Long id;

	@Getter
	@Setter
	private String todo;

	@Getter
	@Setter
	private Boolean completed;

	@Getter
	@Setter
	private Long userId;
}
