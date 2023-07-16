package com.bence.mate.spring.project.order.dto;

import com.bence.mate.spring.project.user.dto.TransactionResponseDto;
import com.bence.mate.spring.project.user.dto.TransactionRequestDto;
import com.bence.mate.spring.project.product.dto.ProductDto;

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
public class RequestContext {

	@Getter
	@Setter
    private PurchaseOrderRequestDto purchaseOrderRequestDto;

	@Getter
	@Setter
	private ProductDto productDto;

	@Getter
	@Setter
	private TransactionRequestDto transactionRequestDto;

	@Getter
	@Setter
	private TransactionResponseDto transactionResponseDto;

    public RequestContext(PurchaseOrderRequestDto purchaseOrderRequestDto) {
        this.purchaseOrderRequestDto = purchaseOrderRequestDto;
    }
}
