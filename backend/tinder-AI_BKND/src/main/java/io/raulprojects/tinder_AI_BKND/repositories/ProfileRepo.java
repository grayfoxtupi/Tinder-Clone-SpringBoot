package io.raulprojects.tinder_AI_BKND.repositories;

import org.springframework.data.mongodb.repository.Aggregation;
import org.springframework.data.mongodb.repository.MongoRepository;

import io.raulprojects.tinder_AI_BKND.profiles.Profile;

public interface ProfileRepo extends MongoRepository<Profile,String>{

  @Aggregation(pipeline = {"{$sample: {size: 1}}"})
  public Profile getRandomProfile();
}
