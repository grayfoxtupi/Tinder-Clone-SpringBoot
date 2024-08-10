package io.raulprojects.tinder_AI_BKND.controller;

import org.springframework.web.bind.annotation.RestController;

import io.raulprojects.tinder_AI_BKND.profiles.Profile;
import io.raulprojects.tinder_AI_BKND.repositories.ProfileRepo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;


@RestController
public class ProfileController {
  
  @Autowired
  private ProfileRepo profileRepo;

  @CrossOrigin(origins = "*") // In developer ambience only
  @GetMapping("/profiles/random")
  public Profile getRandomProfile() {
      return profileRepo.getRandomProfile();
  }
  
}
