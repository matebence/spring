package com.bence.mate.spring.project.user.component;

import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bence.mate.spring.project.user.service.TransactionService;
import com.bence.mate.spring.project.user.dto.TransactionResponseDto;
import com.bence.mate.spring.project.user.dto.TransactionRequestDto;
import com.bence.mate.spring.project.user.entity.UserTransaction;

import reactor.core.publisher.Mono;

@Component
public class TransactionHandler {
	
	@Autowired
	private TransactionService transactionService;

	public Mono<ServerResponse> getByUserId(ServerRequest serverRequest) {
		Integer id = Integer.valueOf(serverRequest.pathVariable("id"));

		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(transactionService.getByUserId(id), UserTransaction.class);
	}

	public Mono<ServerResponse> createTransaction(ServerRequest serverRequest) {
		Mono<TransactionRequestDto> transactionDtoMono = serverRequest.bodyToMono(TransactionRequestDto.class);

		return transactionDtoMono.flatMap(u -> 
				ServerResponse.status(HttpStatus.CREATED)
					.contentType(MediaType.APPLICATION_JSON)
					.body(transactionService.createTransaction(u), TransactionResponseDto.class));
	}
}