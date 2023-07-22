package com.bence.mate.spring.dto;

import com.bence.mate.spring.entity.Order;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderCompletedEvent {

	private Order order;
}
