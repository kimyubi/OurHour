package com.ourhours.server.domain.sample.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ourhours.server.domain.sample.domain.entity.Sample;

public interface SampleRepository extends JpaRepository<Sample, Long> {
}
