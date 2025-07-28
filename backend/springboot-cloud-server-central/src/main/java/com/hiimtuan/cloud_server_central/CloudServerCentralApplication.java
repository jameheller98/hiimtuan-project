package com.hiimtuan.cloud_server_central;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.config.server.EnableConfigServer;

@EnableConfigServer
@SpringBootApplication
public class CloudServerCentralApplication {

	public static void main(String[] args) {
		SpringApplication.run(CloudServerCentralApplication.class, args);
	}

}
