package com.bence.mate.spring.general.component;

import org.springframework.web.reactive.function.server.ServerResponse;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.stereotype.Component;

import java.util.function.BiFunction;
import reactor.core.publisher.Mono;

@Component
public class CalculatorHandler {

    public Mono<ServerResponse> additionHandler(ServerRequest request){
        return process(request, (a, b) -> ServerResponse.ok().bodyValue(a + b));
    }

    public Mono<ServerResponse> subtractionHandler(ServerRequest request){
        return process(request, (a, b) -> ServerResponse.ok().bodyValue(a - b));
    }

    public Mono<ServerResponse> multiplicationHandler(ServerRequest request){
        return process(request, (a, b) -> ServerResponse.ok().bodyValue(a * b));
    }

    public Mono<ServerResponse> divisionHandler(ServerRequest request){
        return process(request, (a, b) ->
            b != 0 ? ServerResponse.ok().bodyValue(a / b) :
                     ServerResponse.badRequest().bodyValue("b can not be 0")
        );
    }

    private Mono<ServerResponse> process(ServerRequest request, BiFunction<Integer, Integer, Mono<ServerResponse>> opLogic){
        int a = getValue(request, "a");
        int b = getValue(request, "b");
        
        return opLogic.apply(a, b);
    }

    private int getValue(ServerRequest request, String key){
        return Integer.parseInt(request.pathVariable(key));
    }
}
