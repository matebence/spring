package com.bence.mate.spring.project.product.util;

import com.bence.mate.spring.project.product.dto.ProductDto;
import com.bence.mate.spring.project.product.entity.Product;

import org.springframework.beans.BeanUtils;

public class EntityDtoUtil {

    public static ProductDto toDto(Product product){
        ProductDto dto = new ProductDto();
        BeanUtils.copyProperties(product, dto);
        
        return dto;
    }

    public static Product toEntity(ProductDto productDto){
        Product product = new Product();
        BeanUtils.copyProperties(productDto, product);
        
        return product;
    }
}
