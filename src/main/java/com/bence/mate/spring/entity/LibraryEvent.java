package com.bence.mate.spring.entity;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.EnumType;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class LibraryEvent {

    @Id
    @Getter
    @Setter
    @GeneratedValue
    private Integer libraryEventId;

    @Getter
    @Setter
    @Enumerated(EnumType.STRING)
    private LibraryEventType libraryEventType;

    @Getter
    @Setter
    @OneToOne(mappedBy = "libraryEvent", cascade = {CascadeType.ALL})
    private Book book;
}
