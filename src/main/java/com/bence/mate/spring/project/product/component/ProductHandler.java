package com.bence.mate.spring.project.product.component;

import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;

import com.bence.mate.spring.project.product.service.ProductService;
import com.bence.mate.spring.project.product.dto.ProductDto;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
public class ProductHandler {

	@Autowired
	private Flux<ProductDto> flux;

	@Autowired
	private ProductService productService;

	public Mono<ServerResponse> all(ServerRequest serverRequest) {
		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON).body(productService.getAll(),
				ProductDto.class);
	}

	public Mono<ServerResponse> getByPriceRange(ServerRequest serverRequest) {
		Integer min = Integer.valueOf(serverRequest.pathVariable("min"));
		Integer max = Integer.valueOf(serverRequest.pathVariable("max"));

		return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(productService.getProductByPriceRange(min, max), ProductDto.class);
	}

	public Mono<ServerResponse> getProductUpdates(ServerRequest serverRequest) {
		Integer maxPrice = Integer.valueOf(serverRequest.pathVariable("maxPrice"));

		return ServerResponse.ok().contentType(MediaType.TEXT_EVENT_STREAM)
				.body(BodyInserters.fromPublisher(flux.filter(dto -> dto.getPrice() <= maxPrice), ProductDto.class));
	}

	public Mono<ServerResponse> getProductById(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(productService.getProductById(id), ProductDto.class);
	}

	public Mono<ServerResponse> insertProduct(ServerRequest serverRequest) {
		Mono<ProductDto> productDtoMono = serverRequest.bodyToMono(ProductDto.class);

		return ServerResponse.status(HttpStatus.CREATED)
				.contentType(MediaType.APPLICATION_JSON)
				.body(productService.insertProduct(productDtoMono), ProductDto.class);
	}

	public Mono<ServerResponse> updateProduct(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");
		Mono<ProductDto> productDtoMono = serverRequest.bodyToMono(ProductDto.class);

		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(productService.updateProduct(id, productDtoMono), ProductDto.class);
	}

	public Mono<ServerResponse> deleteProduct(ServerRequest serverRequest) {
		String id = serverRequest.pathVariable("id");

		return ServerResponse.ok()
				.contentType(MediaType.APPLICATION_JSON)
				.body(productService.deleteProduct(id), Void.class);
	}
}
