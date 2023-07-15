package com.bence.mate.spring.project.product.dto;

import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
public class ProductDto {

	@Getter
	@Setter
    private String id;
    
	@Getter
	@Setter
	private String description;
    
	@Getter
	@Setter
	private Integer price;

    public ProductDto(String description, Integer price) {
        this.description = description;
        this.price = price;
    }

	@Override
	public String toString() {
		return "ProductDto [id=" + id + ", description=" + description + ", price=" + price + "]";
	}
}
