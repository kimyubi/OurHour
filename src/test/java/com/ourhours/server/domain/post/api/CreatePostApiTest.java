package com.ourhours.server.domain.post.api;

import static com.ourhours.server.global.exception.ExceptionConstant.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.context.SecurityContextHolder;

import com.ourhours.server.domain.member.entity.Member;
import com.ourhours.server.domain.member.repository.MemberRepository;
import com.ourhours.server.domain.post.dto.request.CreatePostRequest;
import com.ourhours.server.domain.post.service.CreatePostService;
import com.ourhours.server.global.model.ApiResponse;
import com.ourhours.server.global.model.jwt.dto.request.JwtAuthenticationRequestDto;
import com.ourhours.server.global.model.security.JwtAuthentication;

class CreatePostApiTest {

	@InjectMocks
	private CreatePostApi createPostApi;

	@Mock
	private CreatePostService createPostService;

	@Mock
	private MemberRepository memberRepository;

	@BeforeEach
	public void setup() {
		MockitoAnnotations.openMocks(this);
	}

	@DisplayName("로그인하지 않은 사용자가 API를 요청하는 경우 INVALID_ACCESS를 반환한다.")
	@Test
	void createPostTest_whenUserIsNotLoggedIn() {
		//given
		Long memberId = 1L;
		mockAuthentication(memberId);

		// when
		CreatePostRequest request = new CreatePostRequest("content", List.of());
		ApiResponse response = createPostApi.createPost(request);

		// then
		assertEquals(response.getCode(), INVALID_ACCESS.getCode());
		assertEquals(response.getMessage(), INVALID_ACCESS.getMessage());
	}

	@DisplayName("로그인한 사용자가 API를 요청하는 경우 CreatePostService 클래스의 createPost 메소드가 호출된다.")
	@Test
	void createPostTest_whenUserIsLoggedIn() {
		// given
		Long memberId = 1L;
		Member member = new Member();
		member.setIdForMockTest(memberId);

		mockAuthentication(memberId);
		when(memberRepository.findById(anyLong())).thenReturn(Optional.of(member));

		// when
		CreatePostRequest request = new CreatePostRequest("content", List.of());
		createPostApi.createPost(request);

		// then
		verify(createPostService, times(1)).createPost(any(), anyLong());
	}

	private void mockAuthentication(Long memberId) {
		JwtAuthentication jwtAuthentication = new JwtAuthentication(
			new JwtAuthenticationRequestDto("", memberId)
		);
		SecurityContextHolder.getContext().setAuthentication(jwtAuthentication);
	}

}
