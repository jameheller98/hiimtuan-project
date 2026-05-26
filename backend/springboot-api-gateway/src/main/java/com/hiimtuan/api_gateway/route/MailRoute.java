package com.hiimtuan.api_gateway.route;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.uri;
import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.path;

public class MailRoute {
    static public RouterFunction<ServerResponse> mailFunction() {
        return route("mail-service")
                .route(path("/api/v1/mail/**"), http())
                .before(uri("http://localhost:8100/api/v1/mail"))
                .build();
    }
}
