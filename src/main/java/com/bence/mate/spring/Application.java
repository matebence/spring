package com.bence.mate.spring;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.data.r2dbc.repository.config.EnableR2dbcRepositories;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;

import com.bence.mate.spring.project.product.repository.ProductRepository;
import com.bence.mate.spring.project.user.repository.UserRepository;
import com.bence.mate.spring.project.product.entity.Product;
import com.bence.mate.spring.project.user.entity.User;

import java.util.concurrent.ThreadLocalRandom;
import java.time.Duration;
import java.util.List;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@SpringBootApplication
@EntityScan("com.bence.mate.spring.general.entity")
@EnableMongoRepositories("com.bence.mate.spring.project.product.entity")
@EnableR2dbcRepositories({"com.bence.mate.spring.general.repository", "com.bence.mate.spring.project"})
@ComponentScan({"com.bence.mate.spring", "com.bence.mate.spring.general", "com.bence.mate.spring.project"})
public class Application implements CommandLineRunner {

	
	@Autowired
	private UserRepository userRepository;

	@Autowired
	private ProductRepository productRepository;

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		userRepository.saveAll(List.of(
				User.builder().name("sam").balance(1000).build(),
				User.builder().name("mike").balance(1200).build(),
				User.builder().name("jake").balance(800).build(),
				User.builder().name("marshal").balance(2000).build()
		)).blockLast(Duration.ofSeconds(10));
		
		Product p1 = Product.builder().description("4k-tv").price(1000).build();
		Product p2 = Product.builder().description("slr-camera").price(750).build();
		Product p3 = Product.builder().description("iphone").price(800).build();
		Product p4 = Product.builder().description("headphone").price(100).build();

		Flux.just(p1, p2, p3, p4).concatWith(newProducts()).flatMap(p -> productRepository.insert(Mono.just(p)))
				.subscribe(System.out::println);
	}

	private Flux<Product> newProducts() {
		return Flux.range(1, 1000).delayElements(Duration.ofSeconds(2))
				.map(i -> Product.builder().description("product-" + i).price(ThreadLocalRandom.current().nextInt(10, 100)).build());
	}
}
