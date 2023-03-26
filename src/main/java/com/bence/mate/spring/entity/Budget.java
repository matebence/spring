package com.bence.mate.spring.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.math.BigDecimal;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "BUDGET")
public class Budget implements Serializable {

	@Id
	@Getter
	@Setter
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Getter
	@Setter
	@Column(name = "NAME")
	private String name;

	@Getter
	@Setter
	@Column(name = "GOAL_AMOUNT")
	private BigDecimal goalAmount;

	@Getter
	@Setter
	@Column(name = "PERIOD")
	private String period;
}
