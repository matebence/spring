package com.bence.mate.spring.project.user.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class UserDto {

	@Getter
	@Setter
    private Integer id;

	@Getter
	@Setter
	private String name;

	@Getter
	@Setter
    private Integer balance;
}
