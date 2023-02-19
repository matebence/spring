package com.bence.mate.spring.entity;

import javax.persistence.ElementCollection;
import javax.persistence.CollectionTable;
import javax.persistence.GeneratedValue;
import javax.persistence.JoinColumn;
import javax.persistence.FetchType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Builder
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {

	@Id
	@GeneratedValue
	@Column(name = "user_id")
	private Integer id;

	@Column(name = "user_name")
	private String name;

	@Column(name = "user_passwd")
	private String password;

	@Column(name = "user_email")
	private String email;

	@Column(name = "user_role")
	@ElementCollection(fetch = FetchType.EAGER)
	@CollectionTable(name = "roles", joinColumns = @JoinColumn(name = "user_id"))
	private List<String> roles;
}
