package com.bence.mate.spring.resource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bence.mate.spring.service.PersonService;
import com.bence.mate.spring.entity.Person;

@RestController
@RequestMapping(path = "/persons", produces = MediaType.APPLICATION_JSON_VALUE, consumes = MediaType.APPLICATION_JSON_VALUE)
public class PersonResource {

	@Autowired
	private PersonService personService;

	@PostMapping
	public ResponseEntity<Person> save(@RequestBody Person person) {
		Person persisted = personService.save(person);

		return ResponseEntity.status(HttpStatus.CREATED).body(persisted);
	}

	@GetMapping(value = "/{id}")
	public ResponseEntity<Person> findById(@PathVariable(value = "id") Long id) {
		Person person = personService.find(id).orElseThrow(NullPointerException::new);

		return ResponseEntity.status(HttpStatus.OK).body(person);
	}

	@PutMapping(value = "/{id}")
	public ResponseEntity<Person> update(@RequestBody Person person, @PathVariable(value = "id") Long id) {
		Person updated = personService.update(person, id);

		return ResponseEntity.status(HttpStatus.OK).body(updated);
	}

	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable(value = "id") Long id) {
		personService.delete(id);

		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
}
