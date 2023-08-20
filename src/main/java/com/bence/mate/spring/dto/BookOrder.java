package com.bence.mate.spring.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookOrder implements Serializable {

	@Getter
	@Setter
	private Book book;
	
	@Getter
	@Setter
	private Customer customer;

	@Getter
	@Setter
    private String bookOrderId;

	@Override
	public String toString() {
		return "BookOrder [book=" + book + ", customer=" + customer + ", bookOrderId=" + bookOrderId + "]";
	}
}
