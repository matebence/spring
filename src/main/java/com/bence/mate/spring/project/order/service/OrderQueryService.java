package com.bence.mate.spring.project.order.service;

import com.bence.mate.spring.project.order.repository.PurchaseOrderRepository;
import com.bence.mate.spring.project.order.dto.PurchaseOrderResponseDto;
import com.bence.mate.spring.project.order.util.EntityDtoUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import reactor.core.scheduler.Schedulers;
import reactor.core.publisher.Flux;

@Service
public class OrderQueryService {

    @Autowired
    private PurchaseOrderRepository orderRepository;

    public Flux<PurchaseOrderResponseDto> getProductsByUserId(int userId){
        return orderRepository.findByUserId(userId) 
                .map(EntityDtoUtil::getPurchaseOrderResponseDto)
                .subscribeOn(Schedulers.boundedElastic()); 
    }
}
