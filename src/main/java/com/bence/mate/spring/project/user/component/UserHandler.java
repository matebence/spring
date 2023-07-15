package com.bence.mate.spring.project.user.component;

import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bence.mate.spring.project.user.service.UserService;
import com.bence.mate.spring.project.user.dto.UserDto;

import reactor.core.publisher.Mono;

@Component
public class UserHandler {
	
	@Autowired
	private UserService userService;

	public Mono<ServerResponse> all(ServerRequest serverRequest) {
		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(userService.all(), UserDto.class);
	}

	public Mono<ServerResponse> getUserById(ServerRequest serverRequest) {
		Integer id = Integer.valueOf(serverRequest.pathVariable("id"));

		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(userService.getUserById(id), UserDto.class);
	}

	public Mono<ServerResponse> createUser(ServerRequest serverRequest) {
		Mono<UserDto> userDtoMono = serverRequest.bodyToMono(UserDto.class);

		return userDtoMono.flatMap(u -> 
				ServerResponse.status(HttpStatus.CREATED)
					.contentType(MediaType.APPLICATION_JSON)
					.body(userService.createUser(userDtoMono), UserDto.class));
	}

	public Mono<ServerResponse> updateUser(ServerRequest serverRequest) {
		Integer id = Integer.valueOf(serverRequest.pathVariable("id"));
		Mono<UserDto> userDtoMono = serverRequest.bodyToMono(UserDto.class);

		return userDtoMono.flatMap(u -> 
				ServerResponse.status(HttpStatus.OK)
					.contentType(MediaType.APPLICATION_JSON)
					.body(userService.updateUser(id, userDtoMono), UserDto.class));
	}

	public Mono<ServerResponse> deleteUser(ServerRequest serverRequest) {
		Integer id = Integer.valueOf(serverRequest.pathVariable("id"));

		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(userService.deleteUser(id), Void.class);
	}
}
