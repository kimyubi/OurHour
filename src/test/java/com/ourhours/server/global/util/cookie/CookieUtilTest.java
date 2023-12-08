package com.ourhours.server.global.util.cookie;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.Duration;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@ExtendWith(MockitoExtension.class)
class CookieUtilTest {
	@Mock
	private HttpServletRequest request;
	@Mock
	private HttpServletResponse response;

	@DisplayName("찾으려는 쿠키가 존재하는 경우, Optional로 감싸 반환한다.")
	@Test
	void testGetCookieWhenCookieExists() {
		//given
		String cookieNameToFind = "cookie2";
		String cookieValueToFind = "value2";

		Cookie[] cookies = new Cookie[] {
			new Cookie("cookie1", "value1"),
			new Cookie(cookieNameToFind, cookieValueToFind),
			new Cookie("cookie3", "value3")
		};

		HttpServletRequest mockRequest = createMockRequestWithCookies(cookies);

		// when
		Optional<Cookie> optionalCookie = CookieUtil.getCookie(mockRequest, cookieNameToFind);

		// then
		assertTrue(optionalCookie.isPresent());
		assertEquals(cookieNameToFind, optionalCookie.get().getName());
		assertEquals(cookieValueToFind, optionalCookie.get().getValue());
		log.info("cookieNameToFind : [{}], cookieValueToFind : [{}]", cookieNameToFind, cookieValueToFind);
		log.info("Cookie name : [{}], Cookie value : [{}]", optionalCookie.get().getName(),
			optionalCookie.get().getValue());
	}

	@DisplayName("찾으려는 쿠키가 존재하는 경우, 빈 Optional 객체를 반환한다.")
	@Test
	void testGetCookieWhenCookieDoesNotExist() {
		// given
		String cookieNameToFind = "nonexistentCookie";

		Cookie[] cookies = new Cookie[] {
			new Cookie("cookie1", "value1"),
			new Cookie("cookie2", "value2"),
			new Cookie("cookie3", "value3")
		};

		HttpServletRequest mockRequest = createMockRequestWithCookies(cookies);

		// when
		Optional<Cookie> result = CookieUtil.getCookie(mockRequest, cookieNameToFind);

		// then
		assertTrue(result.isEmpty());
	}

	@DisplayName("요청에 쿠키가 존재하지 않는 경우, 빈 Optional 객체를 반환한다.")
	@Test
	void testGetCookieWhenRequestHasNoCookies() {
		// given
		String cookieNameToFind = "cookie1";
		HttpServletRequest mockRequest = createMockRequestWithNoCookies();

		// when
		Optional<Cookie> result = CookieUtil.getCookie(mockRequest, cookieNameToFind);

		// then
		assertTrue(result.isEmpty());
	}

	@Test
	void testAddCookie() {
		// given
		String cookieName = "cookie1";
		String cookieValue = "value1";
		Duration maxAge = Duration.ofMinutes(1);

		// when
		CookieUtil.addCookie(response, cookieName, cookieValue, maxAge);

		// then
		verify(response, times(1)).addCookie(argThat(cookie ->
			cookie.getName().equals(cookieName) &&
				cookie.getValue().equals(cookieValue) &&
				cookie.getPath().equals("/") &&
				cookie.getMaxAge() == (int)maxAge.toSeconds() &&
				cookie.isHttpOnly() &&
				cookie.getSecure()
		));
	}

	private HttpServletRequest createMockRequestWithCookies(Cookie[] cookies) {
		when(request.getCookies()).thenReturn(cookies);
		return request;
	}

	private HttpServletRequest createMockRequestWithNoCookies() {
		when(request.getCookies()).thenReturn(null);
		return request;
	}
}
