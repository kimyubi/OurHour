package com.ourhours.server.domain.post.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ourhours.server.domain.post.entity.Post;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
}
