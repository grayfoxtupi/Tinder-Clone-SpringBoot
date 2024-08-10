package io.raulprojects.tinder_AI_BKND.controller;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.UUID;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import io.raulprojects.tinder_AI_BKND.profiles.ChatMsg;
import io.raulprojects.tinder_AI_BKND.profiles.Conversation;
import io.raulprojects.tinder_AI_BKND.repositories.ConversationRepo;
import io.raulprojects.tinder_AI_BKND.repositories.ProfileRepo;
import io.raulprojects.tinder_AI_BKND.requests.CreateConversationRequest;
import io.raulprojects.tinder_AI_BKND.services.ConversationService;

import io.raulprojects.tinder_AI_BKND.profiles.Profile;

@RestController
public class ProjectController {
	
	private ConversationRepo conversationRepo;
	private ProfileRepo profileRepo;
	private ConversationService conversationService;
	
	ProjectController(ConversationRepo conversationRepo, ProfileRepo profileRepo, ConversationService conversationService){
		this.conversationRepo = conversationRepo;
		this.profileRepo = profileRepo;
		this.conversationService = conversationService;
	}
	
	@PostMapping("/conversations")
	public Conversation createNewConversation(@RequestBody CreateConversationRequest request) {
		profileRepo
		.findById(
				request.profileId())
		.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"O perfil " + request.profileId() + "não existe!"));
	
		Conversation conversation = new Conversation(UUID.randomUUID().toString(),request.profileId(),new ArrayList<ChatMsg>());
		
		conversationRepo.save(conversation);
		
		return conversation;
	}// c42bc286-40c9-4a18-9bf6-6b6cd613b743
	
	@CrossOrigin(origins = "*") // In developer ambience only
	@GetMapping("/conversations/{conversationId}")
	public Conversation getConversation(@PathVariable String conversationId) {
		
		Conversation conversation = conversationRepo.findById(conversationId).orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST,"A conversa com id  " + conversationId + "Não existe!"));
		
		return conversation;
	}
	
	@CrossOrigin(origins = "*") // In developer ambience only
	@PostMapping("/conversations/{conversationId}")
	public Conversation addMessageToConversation(@PathVariable String conversationId, @RequestBody ChatMsg message) {
		// Log the incoming request data
		System.out.println("Received conversationId: " + conversationId);
		System.out.println("Received message: " + message);

		// Null check for conversationId
		if (conversationId == null) {
				throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Conversation ID must not be null");
		}

		// Fetch the conversation
		Conversation conversation = conversationRepo.findById(conversationId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "A conversa com o id " + conversationId + " não existe!"));

		// Fetch and validate author
		String authorId = message.authorId();
		String msgTxt = message.msgTxt();
		profileRepo.findById(authorId)
						.orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "O autor com id " + authorId + " não existe!"));


		Profile profile = profileRepo.findById(conversation.authorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a profile with ID " + conversation.authorId()
                ));
    Profile user = profileRepo.findById(message.authorId())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND,
                        "Unable to find a profile with ID " + message.authorId()
                ));
		// Create and add new message
		ChatMsg newMsg = new ChatMsg(authorId, msgTxt, LocalDateTime.now());
		conversation.msgs().add(newMsg);

		//Calls a answer fo the OllamaAI with it's context
		conversationService.generateAIResponse(conversation, profile, user);

		// Save the updated conversation
		conversationRepo.save(conversation);

		// Log the new message
		System.out.println("New message added: " + newMsg);

		return conversation;
}
}
