package com.hiimtuan.api_gateway.route;

import static org.springframework.cloud.gateway.server.mvc.handler.GatewayRouterFunctions.route;
import static org.springframework.cloud.gateway.server.mvc.handler.HandlerFunctions.http;
import static org.springframework.cloud.gateway.server.mvc.filter.BeforeFilterFunctions.*;
import static org.springframework.cloud.gateway.server.mvc.predicate.GatewayRequestPredicates.*;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

public class UserRoute {
     public static RouterFunction<ServerResponse> userFunction() {
        return route("user-service")
                .route(path("/api/v1/user/**"), http())
                .before(uri("http://localhost:8000/api/v1/user"))
                .build();
     }

     public static RouterFunction<ServerResponse> adminFunction() {
        return route("admin-service")
                 .route(path("/api/v1/admin/**"), http())
                 .before(uri("http://localhost:8000/api/v1/admin"))
                 .build();
     }
}
