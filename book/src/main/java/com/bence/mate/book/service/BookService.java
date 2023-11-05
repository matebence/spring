package com.bence.mate.book.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.bence.mate.book.exception.BookNotFoundException;
import com.bence.mate.book.repository.BookRepository;
import com.bence.mate.book.entity.Book;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.List;

@Slf4j
@Service
public class BookService {

    @Autowired
    private BookRepository bookRepository;

    public Book getBookById(Long id) {
        return bookRepository.findById(id).orElseThrow(BookNotFoundException::new);
    }

    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }

    public Book createBook(Book book) {
        return bookRepository.save(book);
    }

    public Book updateBookById(Long id, Book book) {
        Optional<Book> bookToUpdate = bookRepository.findById(id);

        if (bookToUpdate.isPresent()) {
            Book source = bookToUpdate.get();
            source.update(book);
            return bookRepository.save(source);
        }
        throw new BookNotFoundException();
    }

    public void deleteBookById(Long id) {
        Optional<Book> bookToDelete = bookRepository.findById(id);
        bookToDelete.ifPresent(bookRepository::delete);
    }
}
