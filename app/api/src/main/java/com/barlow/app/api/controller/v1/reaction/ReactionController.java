package com.barlow.app.api.controller.v1.reaction;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.barlow.app.support.response.ApiResponse;
import com.barlow.core.domain.Passport;
import com.barlow.core.domain.reaction.Reaction;
import com.barlow.core.domain.reaction.ReactionService;
import com.barlow.core.domain.reaction.ReactionStatus;
import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;
import com.barlow.services.auth.support.annotation.PassportUser;

import jakarta.websocket.server.PathParam;

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
		@PathParam("targetType") String targetType,
		@PathParam("reactionType") String reactionType
	) {
		log.info("Retrieving reaction for targetId: {}, targetType: {}, reaction: {}", targetId, targetType, reactionType);
		Reaction reaction = new Reaction(targetId, ReactionTarget.valueOf(targetType), ReactionType.valueOf(reactionType));
		ReactionStatus status = reactionService.retrieveReactions(passport.getUser(), reaction);
		return ApiResponse.success(ReactionResponse.from(status));
	}

	@PostMapping("/reaction/{targetId}")
	public ApiResponse<Void> reaction(
		@PassportUser Passport passport,
		@PathVariable("targetId") String targetId,
		@PathParam("targetType") String targetType,
		@PathParam("reactionType") String reactionType
	) {
		log.info("Reaction for targetId: {}, targetType: {}, reaction: {}", targetId, targetType, reactionType);
		Reaction reaction = new Reaction(targetId, ReactionTarget.valueOf(targetType), ReactionType.valueOf(reactionType));
		reactionService.react(passport.getUser(), reaction);
		return ApiResponse.success();
	}

	@PostMapping("/reaction-remove/{targetId}")
	public ApiResponse<Void> reactionRemove(
		@PassportUser Passport passport,
		@PathVariable("targetId") String targetId,
		@PathParam("targetType") String targetType,
		@PathParam("reactionType") String reactionType
	) {
		log.info("Removing reaction for targetId: {}, targetType: {}, reaction: {}", targetId, targetType, reactionType);
		Reaction reaction = new Reaction(targetId, ReactionTarget.valueOf(targetType), ReactionType.valueOf(reactionType));
		reactionService.removeReaction(passport.getUser(), reaction);
		return ApiResponse.success();
	}
}
