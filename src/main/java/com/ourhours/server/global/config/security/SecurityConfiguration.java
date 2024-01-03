package com.ourhours.server.global.config.security;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CsrfTokenRequestAttributeHandler;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;

import com.ourhours.server.global.config.security.filter.JwtAuthenticationFilter;
import com.ourhours.server.global.config.security.filter.JwtExceptionHandlerFilter;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

	private static final String[] WHITE_LIST = {
		"/v3/**",
		"/swagger-ui/**",
		"/error",
		"/api/token",
		"/sample/**",
		"/oauth2/**",
	};
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OauthAuthenticationSuccessHandler oAuth2LoginSuccessHandler;

	private final HttpCookieOAuth2AuthorizedClientRepository httpCookieOAuth2AuthorizedClientRepository;

	CorsConfigurationSource corsConfigurationSource() {
		return request -> {
			CorsConfiguration config = new CorsConfiguration();
			config.setAllowedHeaders(Collections.singletonList("*"));
			config.setAllowedMethods(Collections.singletonList("*"));
			config.setAllowedOriginPatterns(Collections.singletonList("https://localhost:3001"));
			config.setAllowCredentials(true);
			return config;
		};
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		CsrfTokenRequestAttributeHandler requestHandler = new CsrfTokenRequestAttributeHandler();
		requestHandler.setCsrfRequestAttributeName(null);

		return httpSecurity
			.cors(corsConfigurer -> corsConfigurer.configurationSource(corsConfigurationSource()))
			.httpBasic(HttpBasicConfigurer::disable)
			.formLogin(FormLoginConfigurer::disable)
			// .csrf(csrfConfigurer -> csrfConfigurer.csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse())
			// 	.csrfTokenRequestHandler(requestHandler))
			.csrf(AbstractHttpConfigurer::disable)
			.sessionManagement(
				sessionConfigurer -> sessionConfigurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
			.authorizeHttpRequests(request -> request
				.requestMatchers(WHITE_LIST).permitAll()
				.anyRequest().authenticated()
			)
			.addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
			.addFilterBefore(jwtExceptionHandlerFilter, JwtAuthenticationFilter.class)

			.oauth2Login(oauth2 -> oauth2
				.authorizationEndpoint(config ->
					config.authorizationRequestRepository(httpCookieOAuth2AuthorizedClientRepository))
				.successHandler(oAuth2LoginSuccessHandler)
				.userInfoEndpoint(userInfo -> userInfo
					.userService(customOAuth2UserService)
				))
			.build();
	}
}
