package com.bence.mate.spring.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
public class FailureRecord {

    @Id
    @Getter
    @Setter
    @GeneratedValue
    private Integer bookId;

    @Getter
    @Setter
    private String topic;

    @Getter
    @Setter
    private Integer key;

    @Getter
    @Setter
    private String errorRecord;

    @Getter
    @Setter
    private Integer partition;

    @Getter
    @Setter
    private Long offsetValue;

    @Getter
    @Setter
    private String exception;

    @Getter
    @Setter
    private String status;
}
