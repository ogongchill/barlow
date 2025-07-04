package com.barlow.core.storage;

import org.springframework.stereotype.Component;

import com.barlow.services.post.view.PostViewCountRepository;

@Component
class PostViewCountRepositoryAdapter implements PostViewCountRepository {

	private final PostViewCountJpaRepository postViewCountJpaRepository;

	PostViewCountRepositoryAdapter(PostViewCountJpaRepository postViewCountJpaRepository) {
		this.postViewCountJpaRepository = postViewCountJpaRepository;
	}

	@Override
	public void increase(String postId) {
		postViewCountJpaRepository.updateViewCount(postId);
	}
}
