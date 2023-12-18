package com.invexdijin.mspaymentgateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@SpringBootApplication
@EnableFeignClients
public class MsPaymentGatewayApplication {

	public static void main(String[] args) {
		SpringApplication.run(MsPaymentGatewayApplication.class, args);
	}

}
