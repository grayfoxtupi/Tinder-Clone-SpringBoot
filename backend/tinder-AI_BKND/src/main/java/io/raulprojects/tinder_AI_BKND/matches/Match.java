package io.raulprojects.tinder_AI_BKND.matches;

import io.raulprojects.tinder_AI_BKND.profiles.Profile;

public record Match(
	
	String id,
	String conversationId,
	Profile profile
	
	) {}
