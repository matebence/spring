package com.bence.mate.spring.entity;

import jakarta.persistence.TableGenerator;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.JoinTable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import java.io.Serializable;
import java.math.BigDecimal;
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
@Table(name = "ACCOUNT")
public class Account implements Serializable {

	public Account(Account account) {
		setUsers(account.getUsers());
		setId(account.getId());
		setInitialBalance(account.getInitialBalance());
		setCurrentBalance(account.getCurrentBalance());
	}

	@Id
	@Getter
	@Setter
	@Column(name = "ID")
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "account_table_generator")
	@TableGenerator(name = "account_table_generator", table = "IFINACES_KEYS", pkColumnName = "PK_NAME", valueColumnName = "PK_VALUE")
	private Long id;

	@Getter
	@Setter
	@ManyToMany(cascade = CascadeType.ALL)
	@JoinTable(name = "USER_ACCOUNT", joinColumns = @JoinColumn(name = "ACCOUNT_ID"), inverseJoinColumns = @JoinColumn(name = "USER_ID"))
	private Set<User> users = new HashSet<>();

	@Getter
	@Setter
	@Column(name = "INITIAL_BALANCE")
	private BigDecimal initialBalance;

	@Getter
	@Setter
	@Column(name = "CURRENT_BALANCE")
	private BigDecimal currentBalance;
}
