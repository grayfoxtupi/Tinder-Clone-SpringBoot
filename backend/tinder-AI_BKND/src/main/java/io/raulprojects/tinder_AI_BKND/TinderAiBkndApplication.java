package io.raulprojects.tinder_AI_BKND;

import org.apache.catalina.connector.Response;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.ai.ollama.api.OllamaApi.ChatRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import io.raulprojects.tinder_AI_BKND.profiles.Gender;
import io.raulprojects.tinder_AI_BKND.profiles.Profile;
import io.raulprojects.tinder_AI_BKND.repositories.ConversationRepo;
import io.raulprojects.tinder_AI_BKND.repositories.MatchRepo;
import io.raulprojects.tinder_AI_BKND.repositories.ProfileRepo;
import io.raulprojects.tinder_AI_BKND.services.ProfileCreationService;
import io.raulprojects.tinder_AI_BKND.profiles.Profile;

@SpringBootApplication
public class TinderAiBkndApplication implements CommandLineRunner{

	@Autowired
	private ProfileRepo profileRepo;

	@Autowired
	private MatchRepo matchRepo;
	
	@Autowired 
	private ConversationRepo conversationRepo;

	@Autowired
	private OllamaChatClient chatClient;

	@Autowired
	private ProfileCreationService profileCreationService;
	
	public static void main(String[] args) {
		SpringApplication.run(TinderAiBkndApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// TODO Auto-generated method stub
		this.cleatProject();
		profileCreationService.createProfiles(0);
		profileCreationService.saveProfilesToDB();
		
	}

	public void cleatProject(){
		conversationRepo.deleteAll();
		matchRepo.deleteAll();
		profileRepo.deleteAll();
	}

}
