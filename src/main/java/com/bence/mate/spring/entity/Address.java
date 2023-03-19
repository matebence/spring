package com.bence.mate.spring.entity;

import jakarta.persistence.Embeddable;
import jakarta.persistence.Column;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Builder
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class Address implements Serializable {

	@Getter
	@Setter
	@Column(name = "USER_ADDRESS_LINE_1")
	private String addressLine1;

	@Getter
	@Setter
	@Column(name = "ADDRESS_LINE_2")
	private String addressLine2;

	@Getter
	@Setter
	@Column(name = "CITY")
	private String city;

	@Getter
	@Setter
	@Column(name = "STATE")
	private String state;

	@Getter
	@Setter
	@Column(name = "ZIP_CODE")
	private String zipCode;
}
