package com.bence.mate.spring.config;

import org.springframework.web.reactive.function.server.RequestPredicate;
import org.springframework.web.reactive.function.server.RequestPredicates;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;

import com.bence.mate.spring.component.CalculatorHandler;
import com.bence.mate.spring.component.OrderHandler;

@Configuration
public class FluxRouter {

	@Autowired
	private OrderHandler orderHandler;
	
    @Autowired
    private CalculatorHandler calculatorHandler;
	
    @Bean
    public RouterFunction<ServerResponse> highLevelRouter(){
        return RouterFunctions.route()
                .path("order", this::getOrderRoutes)
                .path("calculator", this::getCalculatorRoutes)
                .build();
    }
    
	public RouterFunction<ServerResponse> getOrderRoutes() {
		return RouterFunctions
				.route(RequestPredicates.GET("v2/getAll"), orderHandler::getAllOrders)
				.andRoute(RequestPredicates.GET("v2"), orderHandler::getOrderById)
				.andRoute(RequestPredicates.POST("v2").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderHandler::createOrder)
				.andRoute(RequestPredicates.PUT("v2/{id}").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderHandler::updateOrder)
				.andRoute(RequestPredicates.DELETE("v2").and(RequestPredicates.accept(MediaType.APPLICATION_JSON)), orderHandler::deleteOrder);
	}
	
    private RouterFunction<ServerResponse> getCalculatorRoutes(){
        return RouterFunctions.route()
                .GET("{a}/{b}", isOperation("+") , calculatorHandler::additionHandler)
                .GET("{a}/{b}", isOperation("-"), calculatorHandler::subtractionHandler)
                .GET("{a}/{b}", isOperation("*"), calculatorHandler::multiplicationHandler)
                .GET("{a}/{b}", isOperation("/"), calculatorHandler::divisionHandler)
                .GET("{a}/{b}", req -> ServerResponse.badRequest().bodyValue("OP should be + - * /"))
                .build();
    }

    private RequestPredicate isOperation(String operation){
        return RequestPredicates.headers(headers -> operation.equals(headers.asHttpHeaders().toSingleValueMap().get("OP")));
    }
}
