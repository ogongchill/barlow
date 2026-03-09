package com.barlow.app.api.controller.v1.reaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.app.support.response.ApiResponse;
import com.barlow.core.domain.Passport;
import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.domain.reaction.ReactionQuery;
import com.barlow.core.service.reaction.ReactionService;
import com.barlow.core.domain.reaction.ReactionStatus;
import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;
import com.barlow.infra.auth.support.annotation.PassportUser;

@RestController
@RequestMapping("/api/v1/reactions")
public class ReactionController {

	private static final Logger log = LoggerFactory.getLogger(ReactionController.class);

	private final ReactionService reactionService;

	public ReactionController(ReactionService reactionService) {
		this.reactionService = reactionService;
	}

	@GetMapping("/{targetId}")
	public ApiResponse<ReactionResponse> retrieveReaction(
		@PassportUser Passport passport,
		@PathVariable("targetId") String targetId,
		@RequestParam("targetType") String targetType) {
		log.info("Retrieving reaction for targetId: {}, targetType: {}", targetId, targetType);
		ReactionQuery reactionQuery = new ReactionQuery(targetId, ReactionTarget.valueOf(targetType));
		ReactionStatus status = reactionService.retrieveReactions(passport.getUser(), reactionQuery);
		return ApiResponse.success(ReactionResponse.from(status));
	}

	@PostMapping("/{targetId}")
	@ResponseStatus(HttpStatus.CREATED)
	public ApiResponse<Void> reaction(
		@PassportUser Passport passport,
		@PathVariable("targetId") String targetId,
		@RequestBody ReactionRequest request) {
		log.info("Reaction for targetId: {}, targetType: {}, reaction: {}", targetId, request.targetType(),
			request.reactionType());
		Reaction reaction = new Reaction(
			passport.getUserNo(), targetId, request.targetType(), request.reactionType());
		reactionService.react(passport.getUser(), reaction);
		return ApiResponse.success();
	}

	@DeleteMapping("/{targetId}")
	public ApiResponse<Void> reactionRemove(
		@PassportUser Passport passport,
		@PathVariable("targetId") String targetId,
		@RequestParam("targetType") String targetType,
		@RequestParam("reactionType") String reactionType) {
		log.info("Removing reaction for targetId: {}, targetType: {}, reaction: {}", targetId, targetType,
			reactionType);
		Reaction reaction = new Reaction(
			passport.getUserNo(), targetId, ReactionTarget.valueOf(targetType), ReactionType.valueOf(reactionType));
		reactionService.removeReaction(passport.getUser(), reaction);
		return ApiResponse.success();
	}
}
