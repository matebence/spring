package com.bence.mate.book.entity;

import jakarta.persistence.GenerationType;
import jakarta.persistence.CascadeType;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "book")
public class Book {

    @Id
    @Getter
    @Setter
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Getter
    @Setter
    @Column(name = "first_name")
    private String title;

    @Getter
    @Setter
    @OneToMany(cascade = CascadeType.ALL)
    @JoinColumn(name = "authors_id", nullable = true)
    private List<Author> authors = new ArrayList<>();

    public void update(Book book) {
        setTitle(book.getTitle());
    }

    public void addAuthor(Author item) {
        authors.add(item);
    }

    public void addAuthors(List<Author> items) {
        authors.addAll(items);
    }
}
