package com.bence.mate.spring.project.order.repository;

import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;

import com.bence.mate.spring.project.order.entity.PurchaseOrder;

import reactor.core.publisher.Flux;

@Repository
public interface PurchaseOrderRepository extends ReactiveCrudRepository<PurchaseOrder, Integer> {

    Flux<PurchaseOrder> findByUserId(int userId);
}
