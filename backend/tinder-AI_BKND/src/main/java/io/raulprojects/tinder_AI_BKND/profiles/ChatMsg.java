package io.raulprojects.tinder_AI_BKND.profiles;

import java.time.LocalDateTime;

public record ChatMsg(
	
	String authorId,
	String msgTxt,
	LocalDateTime creationDate
	
	){}
