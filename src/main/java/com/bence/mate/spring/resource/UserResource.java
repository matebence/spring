package com.bence.mate.spring.resource;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Controller;
import org.springframework.data.util.Streamable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bence.mate.spring.exception.UserNotFoundException;
import com.bence.mate.spring.repository.UserRepository;
import com.bence.mate.spring.entity.Error;
import com.bence.mate.spring.entity.User;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import lombok.extern.slf4j.Slf4j;

import java.util.List;

@Slf4j
@Controller
@RequestMapping(path = "/users")
@Tag(name = "User REST endpoint")
public class UserResource {

	@Autowired
	private UserRepository userRepository;

	@ResponseBody
	@CacheEvict(value = "user-cache")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Returns a user", description = "Takes id returns single user")
	@RequestMapping(value = "/get/{id}", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public User get(@PathVariable(value = "id") Long id) {
		return userRepository.findById(id).orElseThrow(UserNotFoundException::new);
	}

	@ResponseBody
	@CacheEvict(value = "user-cache")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Returns all users", description = "Without any conditions it returns all the users")
	@RequestMapping(value = "/getAll", method = RequestMethod.GET, produces = MediaType.APPLICATION_JSON_VALUE)
	public List<User> getAll() {
		return Streamable.of(userRepository.findAll()).toList();
	}

	@ResponseBody
	@ResponseStatus(code = HttpStatus.NOT_FOUND)
	@ExceptionHandler({ UserNotFoundException.class })
	public Error userNotFoundExceptionHandler(Exception exception) {
		return new Error(exception.getMessage());
	}
}
