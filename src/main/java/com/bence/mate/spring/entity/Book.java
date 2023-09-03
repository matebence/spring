package com.bence.mate.spring.entity;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Getter
    @Setter
    @NotNull
    private Integer bookId;

    @Getter
    @Setter
    @NotBlank
    private String bookName;

    @Getter
    @Setter
    @NotBlank
    private String bookAuthor;
}	