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
public class Customer implements Serializable {
	
	@Getter
	@Setter
    private String customerId;

	@Getter
	@Setter
    private String fullName;

	@Override
	public String toString() {
		return "Customer [customerId=" + customerId + ", fullName=" + fullName + "]";
	}
}
