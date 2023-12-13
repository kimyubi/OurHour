package com.ourhours.server;

import java.util.Optional;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import com.ourhours.server.domain.sample.domain.entity.Sample;
import com.ourhours.server.domain.sample.repository.SampleRepository;
import com.ourhours.server.global.model.security.JwtAuthentication;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequiredArgsConstructor
public class SampleController {

	private final SampleRepository sampleRepository;

	@GetMapping("/api/user")
	public Long getUserId() {
		JwtAuthentication authentication = (JwtAuthentication)SecurityContextHolder.getContext().getAuthentication();
		return authentication.getMemberId();
	}

	@GetMapping("/sample")
	public Sample saveSample() {
		Sample sample = Sample.builder()
			.content("sample1")
			.build();

		Sample savedSample = sampleRepository.save(sample);
		return savedSample;
	}

	@GetMapping("/sample/update/{id}")
	public Sample updateSample(@PathVariable Long id) {
		Optional<Sample> sampleOptional = sampleRepository.findById(id);
		Sample sample = sampleOptional.get();
		sample.updateContent("new Sample");
		sampleRepository.saveAndFlush(sample);

		Optional<Sample> sampleOptional2 = sampleRepository.findById(id);
		return sampleOptional2.get();

	}

}
