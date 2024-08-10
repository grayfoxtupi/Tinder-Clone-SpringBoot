package io.raulprojects.tinder_AI_BKND.services;

import org.springframework.ai.ollama.OllamaChatClient;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;
import org.springframework.ai.chat.ChatResponse;
import org.springframework.ai.chat.messages.*;
import org.springframework.ai.chat.prompt.ChatOptions;
import org.springframework.ai.chat.prompt.Prompt;


import java.util.ArrayList;
import java.util.List;

import static java.lang.StringTemplate.STR;

import java.time.LocalDateTime;

import io.raulprojects.tinder_AI_BKND.profiles.ChatMsg;
import io.raulprojects.tinder_AI_BKND.profiles.Conversation;
import io.raulprojects.tinder_AI_BKND.profiles.Profile;

@Service
public class ConversationService {

  private OllamaChatClient chatClient;

  public ConversationService(OllamaChatClient chatClient){
    this.chatClient = chatClient;
  }

  public Conversation generateAIResponse(Conversation conversation, Profile profile, Profile user){

   String profileContext = STR."""
                Inpersonate a tinder profile with \{profile.age()} years old \{profile.ethnicity()} \{profile.gender()} called \{profile.firstName()} \{profile.lastName()}
                This is an in-app text conversation between me and the profile.
                Pretend to be the provided person and respond to the conversation as a Tinder user.
                Your bio is: \{profile.bio()} and your Myers Briggs personality type is \{profile.myersBriggsPersonalityType()}. Respond in the role of this person only.
                The message should look like what a Tinder user writes in response to chat. Keep it short and brief. No hashtags or generic messages.
                Be friendly, approachable, and slightly playful.
                Reflect confidence and genuine interest in getting to know the other person.
                Use humor and wit appropriately to make the conversation enjoyable.
                Match the tone of the user's messages—be more casual or serious as needed.
                Avoid generic greetings like "Hi" or "Hey"; instead, ask interesting questions or make personalized comments based on the other person's profile.
                Show genuine curiosity about their hobbies, interests, and background.
                Compliment specific details from their profile to make them feel special.
                Ask open-ended questions to keep the conversation flowing.
                Share interesting anecdotes or experiences related to the topic of conversation.
                Respond promptly to keep the momentum of the chat going.
                Incorporate playful banter, wordplay, or light teasing to add a fun element to the chat.
                Suggest fun activities or ideas for a potential date.
                Always be respectful and considerate of the other person's feelings.
                Avoid controversial or sensitive topics unless the other person initiates them.
                Be mindful of boundaries and avoid overly personal or intrusive questions early in the conversation.
                """;

    //SystemMessage generalContext = new SystemMessage(profileContext);

    List<AbstractMessage> messageContext = conversation.msgs().stream().map(msg -> {
      if(msg.authorId() == profile.id()){
        return new UserMessage(msg.msgTxt());  
      }else{
        return new AssistantMessage(msg.msgTxt());
      }
    }).toList();

    //messageContext.addFirst(new UserMessage("You are simulating a conversation on Tinder as a mature female user. Respond in a natural, respectful manner. In .txt format"));

    List<Message> allMessages = new ArrayList<>();
    //allMessages.add(generalContext);
    allMessages.addAll(messageContext);
    allMessages.add(new UserMessage(profileContext));
   // List<Message> allMessages = new ArrayList<>(messageContext);
    //chatClient.call("Responde as a female tinder userS");

    Prompt prompt = new Prompt(allMessages);
    
    ChatResponse response = chatClient.call(prompt);
    conversation.msgs().add(new ChatMsg(profile.id(),response.getResult().getOutput().getContent(),LocalDateTime.now()));
    
    System.out.println("This is the AI asnwer: ");
    System.out.println(response.getResult().getOutput().getContent());
    
    return conversation;
  }
}
