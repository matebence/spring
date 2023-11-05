package com.bence.mate.book.resource;

import static org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction.clientRegistrationId;

import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;

import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import com.bence.mate.book.config.WebClientConfig;
import com.bence.mate.book.entity.Author;

import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import org.springframework.security.oauth2.jwt.Jwt;
import com.bence.mate.book.service.BookService;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import com.bence.mate.book.entity.Book;

import java.util.List;
import java.net.URI;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping(path = "/books", produces = MediaType.APPLICATION_JSON_VALUE)
public class BookResource {

    @Autowired
    private BookService bookService;

    @Autowired
    private WebClient webClient;

    @PostMapping
    public ResponseEntity<Book> createBook(@RequestBody Book book) {
        Book instance = bookService.createBook(book);

        return ResponseEntity.status(HttpStatus.CREATED).body(instance);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable(value = "id") Long id, @AuthenticationPrincipal Jwt principal, @RegisteredOAuth2AuthorizedClient("external") OAuth2AuthorizedClient client) throws InterruptedException {
        Book book = bookService.getBookById(id);

        URI targetUri = UriComponentsBuilder.fromHttpUrl(WebClientConfig.AUTHOR_SERVICE_DOMAIN)
                .path("/authors")
                .build().toUri();

        List<Author> authors = this.webClient.get()
                    // .headers(headers -> headers.setBearerAuth(principal.getTokenValue()))
                    // .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(client))
                    .uri(targetUri)
                    .attributes(clientRegistrationId(WebClientConfig.REGISTRATION_ID))
                    .retrieve()
                    .bodyToFlux(Author.class)
                    .collectList()
                    .block();

         book.addAuthors(authors);
         return ResponseEntity.status(HttpStatus.OK).body(book);
    }

    /* @GetMapping(value = "/{id}")
    public void tokenRelay(@PathVariable(value = "id") Long id, @AuthenticationPrincipal Jwt principal, @RegisteredOAuth2AuthorizedClient("external") OAuth2AuthorizedClient client) throws InterruptedException {
        URI targetUri = UriComponentsBuilder.fromHttpUrl(AUTHOR_SERVICE_DOMAIN)
                .path("/authors")
                .build().toUri();

        while (true) {
            this.webClient.get()
                    // .headers(headers -> headers.setBearerAuth(principal.getTokenValue()))
                    // .attributes(ServletOAuth2AuthorizedClientExchangeFilterFunction.oauth2AuthorizedClient(client))
                    .uri(targetUri)
                    .attributes(clientRegistrationId(WebClientConfig.REGISTRATION_ID))
                    .retrieve()
                    .bodyToFlux(Author.class)
                    .collectList()
                    .block();
            Thread.sleep(5000);
        }
    }*/

    @GetMapping("/token")
    public ResponseEntity<Jwt> getTokenInfo(@AuthenticationPrincipal Jwt principal) {
        return ResponseEntity.status(HttpStatus.OK).body(principal);
    }

    @GetMapping("/client")
    public ResponseEntity<OAuth2AuthorizedClient> getClientInfo(@RegisteredOAuth2AuthorizedClient("external") OAuth2AuthorizedClient client) {
        return ResponseEntity.status(HttpStatus.OK).body(client);
    }
}
