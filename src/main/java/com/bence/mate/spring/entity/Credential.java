package com.bence.mate.spring.entity;

import org.springframework.hateoas.RepresentationModel;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "CREDENTIAL")
@JsonIgnoreProperties(value = { "_links" })
public class Credential extends RepresentationModel<Credential> implements Serializable {

	public Credential(Credential credential) {
		setId(credential.getId());
		setUser(credential.getUser());
		setUsername(credential.getUsername());
		setPassword(credential.getPassword());
	}

	@Id
	@Getter
	@Setter
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.AUTO)
	public Long id;

	@Getter
	@Setter
	@JsonIgnore
	@JoinColumn(name = "USER_ID")
	@OneToOne(cascade = CascadeType.ALL)
	public User user;

	@Getter
	@Setter
	@Column(name = "USERNAME")
	private String username;

	@Getter
	@Setter
	@Column(name = "PASSWORD")
	private String password;
}
