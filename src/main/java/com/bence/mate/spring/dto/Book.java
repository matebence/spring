package com.bence.mate.spring.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Setter;
import lombok.Getter;

import java.io.Serializable;

@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book implements Serializable {

	@Getter
	@Setter
    private String bookId;

	@Getter
	@Setter
    private String title;

	@Override
	public String toString() {
		return "Book [bookId=" + bookId + ", title=" + title + "]";
	}
}
