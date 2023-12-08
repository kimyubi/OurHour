package com.ourhours.server.global.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.annotation.web.configurers.HttpBasicConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

import com.ourhours.server.global.config.security.filter.JwtAuthenticationFilter;
import com.ourhours.server.global.config.security.filter.JwtExceptionHandlerFilter;
import com.ourhours.server.global.util.oauth.CustomOAuth2UserService;
import com.ourhours.server.global.util.oauth.OauthAuthenticationSuccessHandler;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity(debug = true)
@RequiredArgsConstructor
public class SecurityConfiguration {

	private static final String[] WHITE_LIST = {
		"/",
		"/error",
		"/api/token",
		"/sample/**",
		"/oauth2/**",
		"/login"
	};
	private final JwtAuthenticationFilter jwtAuthenticationFilter;
	private final JwtExceptionHandlerFilter jwtExceptionHandlerFilter;
	private final CustomOAuth2UserService customOAuth2UserService;
	private final OauthAuthenticationSuccessHandler oAuth2LoginSuccessHandler;

	private final HttpCookieOAuth2AuthorizedClientRepository httpCookieOAuth2AuthorizedClientRepository;

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
		httpSecurity
			.httpBasic(HttpBasicConfigurer::disable)
			.formLogin(FormLoginConfigurer::disable)
			.csrf(csrfConfigurer -> csrfConfigurer.csrfTokenRepository(new CookieCsrfTokenRepository()))
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
				));

		return httpSecurity.build();
	}
}
