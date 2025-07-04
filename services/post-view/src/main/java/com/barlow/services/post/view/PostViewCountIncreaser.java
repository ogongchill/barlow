package com.barlow.services.post.view;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
class PostViewCountIncreaser {

	private final PostViewCountRepository postViewCountRepository;

	PostViewCountIncreaser(PostViewCountRepository postViewCountRepository) {
		this.postViewCountRepository = postViewCountRepository;
	}

	@Transactional
	public void increaseCount(String postId) {
		postViewCountRepository.increase(postId);
	}
}
