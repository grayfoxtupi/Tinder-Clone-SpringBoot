package io.raulprojects.tinder_AI_BKND.profiles;

import java.util.List;

public record Conversation (
	String id,
	String authorId,
	List<ChatMsg> msgs
		
	){}
