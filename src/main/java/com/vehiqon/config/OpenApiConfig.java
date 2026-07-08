package com.vehiqon.config;

import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Vehiqon API",
                version = "1.0",
                description = "REST API documentation for the Vehiqon application.",
                contact = @Contact(
                        name = "Vehiqon Team",
                        email = "support@vehiqon.com"
                ),
                license = @License(
						name = "Vehiqon"
//						url = "https://github.com/DorcasOJ/mini-java-projects/"
				)

        ),
        externalDocs = @ExternalDocumentation(
                description = "The Vehiqon App Documentation"
//				url = "https://github.com/DorcasOJ/mini-java-projects/"
        ),
//        servers = {
//                @Server(
//                        url = "http://localhost:8085/api",
//                        description = "Local Development Server"
//                )
//        },
        security = @SecurityRequirement(name = "bearerAuth")
)
@SecurityScheme(
        name = "bearerAuth",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        description = "Enter your JWT access token. Example: eyJhbGciOiJIUzI1NiJ9..."
)
public class OpenApiConfig {
}
