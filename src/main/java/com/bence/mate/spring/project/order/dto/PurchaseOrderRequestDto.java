package com.bence.mate.spring.project.order.dto;

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
public class PurchaseOrderRequestDto {

	@Getter
	@Setter
    private Integer userId;

	@Getter
	@Setter
    private String productId;
}
