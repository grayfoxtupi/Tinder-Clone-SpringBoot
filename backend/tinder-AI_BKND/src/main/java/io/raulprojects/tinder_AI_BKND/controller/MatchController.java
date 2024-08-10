package io.raulprojects.tinder_AI_BKND.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.raulprojects.tinder_AI_BKND.matches.Match;
import io.raulprojects.tinder_AI_BKND.profiles.ChatMsg;
import io.raulprojects.tinder_AI_BKND.profiles.Conversation;
import io.raulprojects.tinder_AI_BKND.profiles.Profile;
import io.raulprojects.tinder_AI_BKND.repositories.ConversationRepo;
import io.raulprojects.tinder_AI_BKND.repositories.MatchRepo;
import io.raulprojects.tinder_AI_BKND.repositories.ProfileRepo;
import io.raulprojects.tinder_AI_BKND.requests.MatchRequest;

@RestController
public class MatchController {
	
	private ProfileRepo profileRepo;
	private ConversationRepo conversationRepo;
	private MatchRepo matchRepo;
	
	public MatchController(ProfileRepo profileRepo, ConversationRepo conversationRepo, MatchRepo matchRepo) {
		this.profileRepo = profileRepo;
		this.conversationRepo = conversationRepo;
		this.matchRepo = matchRepo;
	}
	
	@CrossOrigin(origins = "*") // In developer ambience only
	@PostMapping("/matches")
	public Match createNewMatch(@RequestBody MatchRequest request) {
		String profileId = request.profileId();
		
		Profile profile = profileRepo.findById(profileId)
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Não há um perfil com este id: " + profileId));
		
		Conversation newConversation = 
				new Conversation(
						UUID.randomUUID().toString(),
						profileId,
						new ArrayList<ChatMsg>());
		
		Match newMatch = 
				new Match(
						UUID.randomUUID().toString(),
						newConversation.id(),
						profile
						);
		
		conversationRepo.save(newConversation);
		matchRepo.save(newMatch);
		
		return newMatch;
		
	}
	
	@CrossOrigin(origins = "*") // In developer ambience only
	@GetMapping("/matches")
	public List<Match> getAllMatches(){
		return matchRepo.findAll();
	}

}
