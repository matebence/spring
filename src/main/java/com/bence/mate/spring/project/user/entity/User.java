package com.bence.mate.spring.project.user.entity;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Table("users")
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
	@Getter
	@Setter
    @Column("id")
    private Integer id;
    
	@Getter
	@Setter
    @Column("name")
    private String name;
	
	@Getter
	@Setter
    @Column("balance")
    private Integer balance;
}
