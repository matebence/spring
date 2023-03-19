package com.bence.mate.spring.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.RequestEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.hateoas.Link;

import com.bence.mate.spring.exception.ItemNotFoundException;
import com.bence.mate.spring.repository.CredentialRepository;
import com.bence.mate.spring.entity.Credential;
import com.bence.mate.spring.entity.Account;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

@Slf4j
@RestController
@Tag(name = "Credential REST endpoint")
@RequestMapping(path = "/credentials", produces = MediaType.APPLICATION_JSON_VALUE)
public class CredentialResource {

	@Autowired
	private CredentialRepository credentialRepository;

	@GetMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Gets a credential by Id", description = "Takes a id as input")
	public EntityModel<Credential> getCredentialById(@PathVariable(value = "id") Long id) {
		Credential instance = credentialRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Credential not found"));
		
		Link selfLink = linkTo(methodOn(getClass()).getCredentialById(id)).withSelfRel();
		Link allLink = linkTo(methodOn(getClass()).getAllCredentials()).withRel("getAll");
		
		return EntityModel.of(instance, selfLink, allLink);
	}

	@GetMapping
	@ResponseStatus(code = HttpStatus.OK)
	@Operation(summary = "Returns all credentials", description = "No input required")
	public CollectionModel<Credential> getAllCredentials() {
		Iterable<Credential> credentials = credentialRepository.findAll();
		for (Credential credential : credentials) {
			Link selfLink = linkTo(methodOn(getClass()).getCredentialById(credential.getId())).withSelfRel();
			Link userLink = linkTo(methodOn(UserResource.class).get(credential.getUser().getId())).withRel("user");
			credential.add(selfLink, userLink);
		}

		Link link = linkTo(CredentialResource.class).withSelfRel();
		CollectionModel<Credential> result = CollectionModel.of(credentials, link);

		return result;
	}

	@ResponseStatus(code = HttpStatus.CREATED)
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Creates a credential", description = "Takes a object as input")
	public EntityModel<Credential> createCredential(RequestEntity<Credential> requestEntity) {
		Credential credential = requestEntity.getBody();
		Credential instance = credentialRepository.save(credential);
		
		Link getLink = linkTo(methodOn(getClass()).getCredentialById(instance.getId())).withRel("get");
		
		return EntityModel.of(instance, getLink);
	}

	@ResponseStatus(code = HttpStatus.OK)
	@PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	@Operation(summary = "Updates a credential by Id", description = "Takes an id and a object as input")
	public EntityModel<Credential> updateCredentialById(@PathVariable(value = "id") Long id, @RequestBody Account account) {
		Credential instance = credentialRepository.findById(id).map(Credential::new).orElseThrow(() -> new ItemNotFoundException("Credential not found"));
		credentialRepository.save(instance);
		Link getLink = linkTo(methodOn(getClass()).getCredentialById(instance.getId())).withRel("get");
		
		return EntityModel.of(instance, getLink);
	}

	@DeleteMapping(value = "/{id}")
	@ResponseStatus(code = HttpStatus.NO_CONTENT)
	@Operation(summary = "Deletes a credential by Id", description = "Takes a id as input")
	public void deleteCredentialById(@PathVariable(value = "id") Long id) {
		Credential instance = credentialRepository.findById(id).orElseThrow(() -> new ItemNotFoundException("Credential not found"));
		credentialRepository.delete(instance);
	}
}
