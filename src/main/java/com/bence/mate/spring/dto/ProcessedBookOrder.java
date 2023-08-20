package com.bence.mate.spring.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

import java.io.Serializable;
import java.util.Date;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ProcessedBookOrder implements Serializable {

	@Getter
	@Setter
    private BookOrder bookOrder;
    
	@Getter
	@Setter
    private Date processingDateTime;
    
	@Getter
	@Setter
    private Date expectedShippingDateTime;
}
