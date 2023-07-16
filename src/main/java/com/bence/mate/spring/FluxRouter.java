package com.bence.mate.spring;

import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.bence.mate.spring.project.order.component.PurchaseOrderHandler;
import com.bence.mate.spring.project.user.component.TransactionHandler;
import com.bence.mate.spring.project.product.component.ProductHandler;
import com.bence.mate.spring.general.component.CalculatorHandler;
import com.bence.mate.spring.project.user.component.UserHandler;
import com.bence.mate.spring.general.component.OrderHandler;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
public class FluxRouter {

	@Autowired
	private UserHandler userHandler;

	@Autowired
	private OrderHandler orderHandler;

	@Autowired
	private ProductHandler productHandler;

	@Autowired
	private CalculatorHandler calculatorHandler;
	
	@Autowired
	private TransactionHandler transactionHandler;
	
	@Autowired
	private PurchaseOrderHandler purchaseOrderHandler;

	@Bean
	public RouterFunction<ServerResponse> highLevelRouter() {
		return RouterFunctions.route()
				.path("user", this::getUserResource)
				.path("order", this::getOrderRoutes)
				.path("product", this::getProductResource)
				.path("purchase/order", this::getPurchaseOrders)
				.path("user/transaction", this::getUserTransactionResource)
				.path("calculator", this::getCalculatorRoutes).build();
	}

	public RouterFunction<ServerResponse> getUserResource() {
		return RouterFunctions.route(RequestPredicates.GET("all"), userHandler::all)
				.andRoute(RequestPredicates.GET("{id}"), userHandler::getUserById)
				.andRoute(RequestPredicates.POST("").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userHandler::createUser)
				.andRoute(RequestPredicates.PUT("{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), userHandler::updateUser)
				.andRoute(RequestPredicates.DELETE("{id}"), userHandler::deleteUser);
	}
	
	public RouterFunction<ServerResponse> getOrderRoutes() {
		return RouterFunctions.route(RequestPredicates.GET("getAll"), orderHandler::getAllOrders)
				.andRoute(RequestPredicates.GET(""), orderHandler::getOrderById)
				.andRoute(RequestPredicates.POST("").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderHandler::createOrder)
				.andRoute(RequestPredicates.PUT("{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderHandler::updateOrder)
				.andRoute(RequestPredicates.DELETE("").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderHandler::deleteOrder);
	}
	
	public RouterFunction<ServerResponse> getProductResource() {
		return RouterFunctions.route(RequestPredicates.GET("all"), productHandler::all)
				.andRoute(RequestPredicates.GET("price-range"), productHandler::getByPriceRange)
				.andRoute(RequestPredicates.GET("stream/{maxPrice}"), productHandler::getProductUpdates)
				.andRoute(RequestPredicates.GET("{id}"), productHandler::getProductById)
				.andRoute(RequestPredicates.POST("").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::insertProduct)
				.andRoute(RequestPredicates.PUT("{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), productHandler::updateProduct)
				.andRoute(RequestPredicates.DELETE("{id}"), productHandler::deleteProduct);
	}

	public RouterFunction<ServerResponse> getPurchaseOrders() {
		return RouterFunctions.route(RequestPredicates.GET(""), purchaseOrderHandler::getOrdersByUserId)
				.andRoute(RequestPredicates.POST("").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), purchaseOrderHandler::order);
	}
	
	public RouterFunction<ServerResponse> getUserTransactionResource() {
		return RouterFunctions.route(RequestPredicates.GET("{id}"), transactionHandler::getByUserId)
				.andRoute(RequestPredicates.POST("").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), transactionHandler::createTransaction);
	}
	
	private RouterFunction<ServerResponse> getCalculatorRoutes() {
		return RouterFunctions.route()
				.GET("{a}/{b}", isOperation("+"), calculatorHandler::additionHandler)
				.GET("{a}/{b}", isOperation("-"), calculatorHandler::subtractionHandler)
				.GET("{a}/{b}", isOperation("*"), calculatorHandler::multiplicationHandler)
				.GET("{a}/{b}", isOperation("/"), calculatorHandler::divisionHandler)
				.GET("{a}/{b}", req -> ServerResponse.badRequest().bodyValue("OP should be + - * /")).build();
	}

	private RequestPredicate isOperation(String operation) {
		return RequestPredicates.headers(headers -> operation.equals(headers.asHttpHeaders().toSingleValueMap().get("OP")));
	}
}
