package com.bence.mate.spring.general.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Table("orders")
@NoArgsConstructor
@AllArgsConstructor
public class Order {

    @Id
	@Getter
	@Setter
    @Column("id")
    private Long id;

	@Getter
	@Setter
    @Column("amount")
    private Double amount;

	@Getter
	@Setter
    @Column("placed_date")
    private LocalDateTime placedDate;
}
