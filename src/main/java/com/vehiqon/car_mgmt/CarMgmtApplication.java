package com.vehiqon.car_mgmt;

import com.vehiqon.car_mgmt.security.config.JwtProperties;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

@SpringBootApplication
@OpenAPIDefinition(
		info = @Info(
				title = "The Vehiqon App",
				description = "Bankend Rest APIs for Vehiqon",
				version = "v1.0",
				contact = @Contact(
						name="Dorcas OJ",
//						email = "hello@mail.com",
						url="https://github.com/DorcasOJ/mini-java-projects/tree/cas-bank/src/main/resources"
				),
				license = @License(
						name = "Vehiqon",
						url = "https://github.com/DorcasOJ/mini-java-projects/"
				)

		),
		externalDocs = @ExternalDocumentation(
				description = "The Vehiqon App Documentation",
				url = "https://github.com/DorcasOJ/mini-java-projects/"
		)
)
@EnableConfigurationProperties(JwtProperties.class)
public class CarMgmtApplication {
	public static void main(String[] args) {
		SpringApplication.run(CarMgmtApplication.class, args);
	}

}
