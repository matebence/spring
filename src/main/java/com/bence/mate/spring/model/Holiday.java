package com.bence.mate.spring.model;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Holiday {

	private String day;

	private String reason;

	private Type type;

	public enum Type {
		FESTIVAL, FEDERAL
	}
}