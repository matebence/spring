package com.bence.mate.spring.entity;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.Valid;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LibraryEvent {

    @Getter
    @Setter
	private Integer libraryEventId;

    @Getter
    @Setter
    private LibraryEventType libraryEventType;
    
    @Valid
    @Getter
    @Setter
    @NotNull
    private Book book;
}
