package com.bence.mate.spring.project.product.entity;

import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	
	@Id
	@Getter
	@Setter
	private String id;
	
	@Getter
	@Setter
	private String description;
	
	@Getter
	@Setter
	private Integer price;

	@Override
	public String toString() {
		return "Product [id=" + id + ", description=" + description + ", price=" + price + "]";
	}
}
