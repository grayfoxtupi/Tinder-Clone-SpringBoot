package io.raulprojects.tinder_AI_BKND.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;

import io.raulprojects.tinder_AI_BKND.profiles.Conversation;

public interface ConversationRepo extends MongoRepository<Conversation, String>{

}
