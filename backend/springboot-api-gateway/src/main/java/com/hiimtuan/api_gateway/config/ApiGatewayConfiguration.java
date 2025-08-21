package com.hiimtuan.api_gateway.config;

import com.hiimtuan.api_gateway.route.UserRoute;
import com.hiimtuan.api_gateway.route.MailRoute;
import com.hiimtuan.api_gateway.route.PostRoute;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.web.servlet.function.RouterFunction;
import org.springframework.web.servlet.function.ServerResponse;

@Configuration
@AllArgsConstructor
public class ApiGatewayConfiguration {
	@Bean
    public RouterFunction<ServerResponse> authRoutes() {
        return UserRoute.userFunction()
                .and(UserRoute.adminFunction());
    }

    @Bean
    public RouterFunction<ServerResponse> mailRoutes() {
        return MailRoute.mailFunction();
    }

    @Bean
    public RouterFunction<ServerResponse> postRoutes() {
        return PostRoute.postFunction();
    }
}
