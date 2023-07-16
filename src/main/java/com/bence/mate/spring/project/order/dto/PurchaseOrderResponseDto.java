package com.bence.mate.spring.project.order.dto;

import com.bence.mate.spring.project.order.entity.OrderStatus;

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
public class PurchaseOrderResponseDto {

	@Getter
	@Setter
    private Integer orderId;

	@Getter
	@Setter
	private Integer userId;

	@Getter
	@Setter
	private String productId;

	@Getter
	@Setter
	private Integer amount;

	@Getter
	@Setter
	private OrderStatus status;
}
