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
public class TransactionResponseDto {

	@Getter
	@Setter
    private Integer userId;

	@Getter
	@Setter
	private Integer amount;

	@Getter
	@Setter
    private TransactionStatus status;
}
