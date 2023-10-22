package com.bence.mate.author.service;

import com.bence.mate.author.exception.AuthorNotFoundException;
import com.bence.mate.author.repository.AuthorRepository;
import com.bence.mate.author.entity.Author;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

import java.util.Optional;
import java.util.List;

@Slf4j
@Service
public class AuthorService {

    @Autowired
    private AuthorRepository authorRepository;

    public Author getAuthorById(Long id) {
        return authorRepository.findById(id).orElseThrow(AuthorNotFoundException::new);
    }

    public List<Author> getAllAuthors() {
        return authorRepository.findAll();
    }

    public Author createAuthor(Author author) {
        return authorRepository.save(author);
    }

    public Author updateAuthorById(Long id, Author author) {
        Optional<Author> authorToUpdate = authorRepository.findById(id);

        if (authorToUpdate.isPresent()) {
            Author source = authorToUpdate.get();
            source.update(author);
            return authorRepository.save(source);
        }
        throw new AuthorNotFoundException();
    }

    public void deleteAuthorById(Long id) {
        Optional<Author> authorToDelete = authorRepository.findById(id);
        authorToDelete.ifPresent(authorRepository::delete);
    }
}
