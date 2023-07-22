package com.bence.mate.spring.resource;

import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bence.mate.spring.service.CustomerService;
import com.bence.mate.spring.entity.ErrorMessage;
import com.bence.mate.spring.entity.Customer;

@RestController
@RequestMapping(path = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
public class CustomerResource {

	@Autowired
	private CustomerService customerService;
	
	@PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
		Customer instance = customerService.register(customer);

		return ResponseEntity.status(HttpStatus.CREATED).body(instance);
	}
	
	@DeleteMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Void> removeCustomer(@PathVariable(value = "id") Long id) {
		
		Customer instance = customerService.find(id).orElseThrow(() -> new RuntimeException("Customer not found"));
		customerService.remove(instance);
		
		return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
	}
	
	@ExceptionHandler({ RuntimeException.class })
	@GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
	public  ResponseEntity<ErrorMessage>  customerNotFoundExceptionHandler(Exception exception) {
		ErrorMessage errorMessage = ErrorMessage.builder().message(exception.getMessage()).build();
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(errorMessage);
	}
}
