package com.bence.mate.spring.project.order.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Table("purchase_orders")
@NoArgsConstructor
@AllArgsConstructor
public class PurchaseOrder {

    @Id
	@Getter
	@Setter
    @Column("id")
    private Integer id;
    
	@Getter
	@Setter
    @Column("product_id")
    private String productId;
    
	@Getter
	@Setter
    @Column("user_id")
    private Integer userId;
	
	@Getter
	@Setter
    @Column("amount")
    private Integer amount;
	
	@Getter
	@Setter
    @Column("status")
    private OrderStatus status;
}
