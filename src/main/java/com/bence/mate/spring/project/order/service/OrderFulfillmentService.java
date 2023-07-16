package com.bence.mate.spring.project.order.service;

import com.bence.mate.spring.project.order.repository.PurchaseOrderRepository;
import com.bence.mate.spring.project.order.dto.PurchaseOrderResponseDto;
import com.bence.mate.spring.project.order.dto.PurchaseOrderRequestDto;
import com.bence.mate.spring.project.order.dto.RequestContext;
import com.bence.mate.spring.project.order.rest.ProductClient;
import com.bence.mate.spring.project.order.util.EntityDtoUtil;
import com.bence.mate.spring.project.order.rest.UserClient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;

@Service
public class OrderFulfillmentService {

	@Autowired
	private UserClient userClient;

	@Autowired
	private ProductClient productClient;

	@Autowired
    private PurchaseOrderRepository purchaseOrderRepository;

    public Mono<PurchaseOrderResponseDto> processOrder(Mono<PurchaseOrderRequestDto> requestDtoMono){
        return requestDtoMono.map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getPurchaseOrder)
                .flatMap(this.purchaseOrderRepository::save) 
                .map(EntityDtoUtil::getPurchaseOrderResponseDto);
        
        /* WITH JPA 
        return requestDtoMono.map(RequestContext::new)
                .flatMap(this::productRequestResponse)
                .doOnNext(EntityDtoUtil::setTransactionRequestDto)
                .flatMap(this::userRequestResponse)
                .map(EntityDtoUtil::getPurchaseOrder)
                .map(this.purchaseOrderRepository::save) // blocking
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic()); //we need to use this because we have a blocking call, this solves it
       */
    }

    private Mono<RequestContext> productRequestResponse(RequestContext rc){
        return this.productClient.getProductById(rc.getPurchaseOrderRequestDto().getProductId())
                .doOnNext(rc::setProductDto)
                .retryWhen(Retry.fixedDelay(5, Duration.ofSeconds(1))) 
                .thenReturn(rc);
    }

    private Mono<RequestContext> userRequestResponse(RequestContext rc){
        return this.userClient.authorizeTransaction(rc.getTransactionRequestDto())
                .doOnNext(rc::setTransactionResponseDto)
                .thenReturn(rc);
    }
}
