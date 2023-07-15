package com.bence.mate.spring.project.user.entity;

import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.annotation.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Builder
@Table("user_transactions")
@NoArgsConstructor
@AllArgsConstructor
public class UserTransaction {

    @Id
	@Getter
	@Setter
    @Column("id")
    private Integer id;
    
	@Getter
	@Setter
    @Column("user_id")
    private Integer userId;
	
	@Getter
	@Setter
    @Column("amount")
    private Integer amount;
	
	@Getter
	@Setter
    @Column("transaction_date")
    private LocalDateTime transactionDate;
}
