package com.bence.mate.author.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;

import com.bence.mate.author.service.AuthorService;
import org.springframework.http.ResponseEntity;
import com.bence.mate.author.entity.Author;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import lombok.extern.slf4j.Slf4j;
import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/authors", produces = MediaType.APPLICATION_JSON_VALUE)
public class AuthorResource {

    @Autowired
    private AuthorService authorService;

    @GetMapping(value = "/{id}")
    public ResponseEntity<Author> getAuthorById(@PathVariable(value = "id") Long id) {
        Author author = authorService.getAuthorById(id);

        return ResponseEntity.status(HttpStatus.OK).body(author);
    }

    @GetMapping
    public ResponseEntity<List<Author>> getAllAuthors() {
        List<Author> allAuthors = authorService.getAllAuthors();

        return ResponseEntity.status(HttpStatus.OK).body(allAuthors);
    }

    @PostMapping
    public ResponseEntity<Author> createAuthor(@RequestBody Author author) {
        Author instance = authorService.createAuthor(author);

        return ResponseEntity.status(HttpStatus.CREATED).body(instance);
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<Author> updateAuthorById(@PathVariable(value = "id") Long id, @RequestBody Author author) {
        Author instance = authorService.updateAuthorById(id, author);

        return ResponseEntity.status(HttpStatus.OK).body(instance);
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> deleteAuthorById(@PathVariable(value = "id") Long id) {
        authorService.deleteAuthorById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}
