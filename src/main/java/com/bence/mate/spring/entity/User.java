package com.bence.mate.spring.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.AttributeOverrides;
import jakarta.persistence.AttributeOverride;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Embedded;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "USER")
public class User implements Serializable {

	@Id
	@Getter
	@Setter
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Getter
	@Setter
	@OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
	private Credential credential;

	@Getter
	@Setter
	@JsonIgnore
	@ManyToMany(cascade = CascadeType.ALL, mappedBy = "users")
	private Set<Account> accounts = new HashSet<>();

	@Getter
	@Setter
	@Column(name = "FIRST_NAME")
	private String lastName;

	@Getter
	@Setter
	@Column(name = "LAST_NAME")
	private String firstName;

	@Getter
	@Setter
	@Embedded
	@Column(name = "ADDRESS")
	@AttributeOverrides({ @AttributeOverride(name = "addressLine1", column = @Column(name = "USER_ADDRESS_LINE_1")) })
	private Address address = new Address();
}
