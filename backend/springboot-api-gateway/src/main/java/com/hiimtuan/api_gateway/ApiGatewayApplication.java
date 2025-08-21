package com.hiimtuan.api_gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.grpc.client.ImportGrpcClients;

@SpringBootApplication(scanBasePackages = {
        "com.hiimtuan.api_gateway",
        "com.hiimtuan.common_service"
})
@ImportGrpcClients(basePackageClasses = ApiGatewayApplication.class)
public class ApiGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApiGatewayApplication.class, args);
	}

}
