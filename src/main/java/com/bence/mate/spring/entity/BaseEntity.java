package com.bence.mate.spring.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.CreatedBy;

import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.Column;

import java.time.LocalDateTime;

import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@SuperBuilder
@MappedSuperclass
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class BaseEntity {

	@Getter
	@Setter
	@CreatedDate
	@Column(name = "created_at", updatable = false)
	private LocalDateTime createdAt;

	@Getter
	@Setter
	@CreatedBy
	@Column(name = "created_by", updatable = false)
	private String createdBy;

	@Getter
	@Setter
	@LastModifiedDate
	@Column(name = "update_at", insertable = false)
	private LocalDateTime updatedAt;

	@Getter
	@Setter
	@LastModifiedBy
	@Column(name = "updated_by", insertable = false)
	private String updatedBy;
}
