package com.bence.mate.spring;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import com.bence.mate.spring.service.BookOrderService;
import com.bence.mate.spring.dto.BookOrder;
import com.bence.mate.spring.dto.Customer;
import com.bence.mate.spring.dto.Book;

@EnableJms
@SpringBootApplication
public class Application implements CommandLineRunner {

	@Autowired
	private BookOrderService bookOrderService;
	
	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);		
	}

	@Override
	public void run(String... args) throws Exception {
		Book book = Book.builder().title("The book").bookId("321").build();
		Customer customer = Customer.builder().fullName("ecneb").customerId("123").build();
		
		bookOrderService.send(BookOrder.builder().book(book).customer(customer).bookOrderId("999").build(), "111", "DELETE");
	}
}
