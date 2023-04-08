package com.bence.mate.spring.rest.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Todo {

	@Getter
	@Setter
	private List<Task> todos;

	@Getter
	@Setter
	private Integer total;

	@Getter
	@Setter
	private Integer skip;

	@Getter
	@Setter
	private Integer limit;
}
