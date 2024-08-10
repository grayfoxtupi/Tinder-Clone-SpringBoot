package io.raulprojects.tinder_AI_BKND.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.raulprojects.tinder_AI_BKND.matches.Match;

public interface MatchRepo extends MongoRepository<Match, String> {
	
	

}
