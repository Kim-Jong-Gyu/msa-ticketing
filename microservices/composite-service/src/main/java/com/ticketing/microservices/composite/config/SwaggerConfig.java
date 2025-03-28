package com.ticketing.microservices.composite.config;

import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;

@Configuration
public class SwaggerConfig {

	@Value("${api.common.version}")           String apiVersion;
	@Value("${api.common.title}")             String apiTitle;
	@Value("${api.common.description}")       String apiDescription;
	@Value("${api.common.termsOfServiceUrl}") String apiTermsOfServiceUrl;
	@Value("${api.common.license}")           String apiLicense;
	@Value("${api.common.licenseUrl}")        String apiLicenseUrl;
	@Value("${api.common.contact.name}")      String apiContactName;
	@Value("${api.common.contact.url}")       String apiContactUrl;
	@Value("${api.common.contact.email}")     String apiContactEmail;

	@Bean
	public OpenAPI openAPI(){
		return new OpenAPI()
			.info(new Info().title(apiTitle)
				.description(apiDescription)
				.version(apiVersion)
				.license(new License().name(apiLicense).url(apiLicenseUrl))
				.contact(new Contact().name(apiContactName).url(apiContactUrl).email(apiContactEmail))
				.termsOfService(apiTermsOfServiceUrl));
	}

	@Bean
	public GroupedOpenApi api(){
		return GroupedOpenApi.builder()
			.group("springdoc-openapi")
			.pathsToMatch("/**")
			.packagesToScan("com.ticketing")
			.build();
	}
}
