package com.vehiqon;

import com.vehiqon.security.jwt.JwtProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
@EnableConfigurationProperties(JwtProperties.class)
@EnableScheduling
public class CarMgmtApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarMgmtApplication.class, args);
	}
}
