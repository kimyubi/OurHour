package com.ourhours.server.global.config.swagger;

import static com.ourhours.server.global.util.jwt.JwtConstant.*;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Component
public class SwaggerConfiguration {
	
	@Bean
	public OpenAPI api() {
		Info info = new Info()
			.title("제목")
			.version("V1.0")
			.contact(new Contact()
				.name("Web Site")
				.url("배포 주소"))
			.license(new License()
				.name("Apache License Version 2.0")
				.url("http://www.apache.org/license/LICENSE-2.0"));

		SecurityScheme tokenAuth = new SecurityScheme()
			.type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.COOKIE).name(JWT_COOKIE_NAME.getValue());

		SecurityScheme uuidAuth = new SecurityScheme()
			.type(SecurityScheme.Type.APIKEY).in(SecurityScheme.In.COOKIE).name(UUID_COOKIE_NAME.getValue());

		SecurityRequirement securityRequirement = new SecurityRequirement().addList("tokenAuth").addList("uuidAuth");

		return new OpenAPI()
			.components(
				new Components().addSecuritySchemes("tokenAuth", tokenAuth).addSecuritySchemes("uuidAuth", uuidAuth))
			.addSecurityItem(securityRequirement)
			.info(info);
	}
}
