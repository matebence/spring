package com.bence.mate.spring.entity;

import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {
    @Id
    @Getter
    @Setter
    private Integer bookId;

    @Getter
    @Setter
    private String bookName;

    @Getter
    @Setter
    private String bookAuthor;

    @Getter
    @Setter
    @OneToOne
    @JoinColumn(name = "libraryEventId")
    private LibraryEvent libraryEvent;
}
