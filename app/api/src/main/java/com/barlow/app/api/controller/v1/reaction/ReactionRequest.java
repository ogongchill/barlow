package com.barlow.app.api.controller.v1.reaction;

import com.barlow.core.enumerate.ReactionTarget;
import com.barlow.core.enumerate.ReactionType;

public record ReactionRequest(ReactionTarget targetType, ReactionType reactionType) {
}
