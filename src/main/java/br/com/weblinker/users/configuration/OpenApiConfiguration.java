package br.com.weblinker.users.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.License;

// link swagger: http://localhost:8000/swagger-ui.html
@Configuration
@OpenAPIDefinition(info =
        @Info(title = "Users Service API",
                version = "v1",
                description = "Documentation of Users Service API"))
public class OpenApiConfiguration {
	
	@Bean
	OpenAPI customOpenAPI() {
		return new OpenAPI()
			.components(new Components())
			.info(
				new io.swagger.v3.oas.models.info.Info()
				.title("Users Service API")
				.version("v1")
				.license(
					new License()
						.name("Apache 2.0")
						.url("http://springdoc.org")
				)
			);
	}
}