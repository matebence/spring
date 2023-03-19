package com.bence.mate.spring.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.MatrixVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.ResponseEntity;
import org.springframework.http.RequestEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bence.mate.spring.exception.ItemNotFoundException;
import com.bence.mate.spring.repository.AccountRepository;
import com.bence.mate.spring.entity.Account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.Hidden;
import lombok.extern.slf4j.Slf4j;

import java.util.stream.Collectors;
import java.math.BigDecimal;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "Account REST endpoint")
@RequestMapping(path = "/accounts", produces = MediaType.APPLICATION_JSON_VALUE)
public class AccountResource {

	@Autowired
	private AccountRepository accountRepository;

	@Hidden
	@GetMapping(value = "/{id}")
	@Cacheable(value = "account-cache", key = "#id")
	public ResponseEntity<Account> getAccountById(@PathVariable(value = "id") Long id, @MatrixVariable(value = "balance", required = false) BigDecimal balance) {
		log.info("{}", balance);
		
		Account account = accountRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Account not found"));
		
		return ResponseEntity.status(HttpStatus.OK).body(account);
	}

	@Hidden
	@GetMapping
	@CacheEvict(value = "account-cache")
	public ResponseEntity<List<Account>> getAllAccounts(@CookieValue(value = "count", required = false) Integer count) {
		log.info("{}", count);
		
		List<Account> accounts = accountRepository.findAll();
		
		return ResponseEntity.status(HttpStatus.OK).body(accounts);
	}

	@Hidden
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creates a account", description = "Takes a object as input")
	public ResponseEntity<Account> createAccount(RequestEntity<Account> requestEntity) {
		HttpHeaders headers = requestEntity.getHeaders();

		headers.forEach((key, value) -> {
			log.info(String.format("Header '%s' = %s", key, value.stream().collect(Collectors.joining("|"))));
		});

		Account account = requestEntity.getBody();
		Account instance = accountRepository.save(account);

		return ResponseEntity.status(HttpStatus.CREATED).body(instance);
	}

	@Hidden
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Account> updateAccountById(@PathVariable(value = "id") Long id, @RequestBody Account account) {
		Account instance = accountRepository.findById(id).map(Account::new).orElseThrow(() -> new ItemNotFoundException("Account not found"));
		accountRepository.save(instance);
		
		return ResponseEntity.status(HttpStatus.OK).body(instance);
	}

	@Hidden
	@PatchMapping(value = "/{id}/{balance}")
	public ResponseEntity<Account> updateAccountBalanceById(@PathVariable(value = "id") Long id, @PathVariable(value = "balance") BigDecimal balance) {
		Account instance = accountRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Account not found"));
		instance.setCurrentBalance(balance);
		accountRepository.save(instance);
		
		return ResponseEntity.status(HttpStatus.OK).body(instance);
	}

	@Hidden
	@CacheEvict(value = "account-cache", key = "#id")
	@DeleteMapping(value = "/{id}", produces = MediaType.APPLICATION_XML_VALUE, consumes = MediaType.APPLICATION_XML_VALUE)
	public ResponseEntity<Void> deleteAccountById(@PathVariable(value = "id") Long id, @RequestHeader(value = "balance", required = false) BigDecimal balance) {
		log.info("{}", balance);
		
		Account instance = accountRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Account not found"));
		accountRepository.delete(instance);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
