import {  User, MessageCircle, X, Heart, Send } from 'lucide-react';
import React,{ useState, useEffect } from 'react';


const fetchRandomProfile = async () => {
  const currentProfile = await fetch('http://localhost:8080/profiles/random');
  if(!currentProfile.ok){
    throw new Error("Failed to fetch the profile")
  }
  return currentProfile.json(); 
}

const fetchMatches = async () => {
  const currentProfile = await fetch('http://localhost:8080/matches');
  if(!currentProfile.ok){
    throw new Error("Failed to fetch the profile")
  }
  return currentProfile.json(); 
}

const fetchConversation = async (conversationId) => {
  const currentProfile = await fetch("http://localhost:8080/conversations/"+conversationId);
  console.log(currentProfile)
  if(!currentProfile.ok){
    throw new Error("Failed to fetch the profile")
  }
  return currentProfile.json(); 
}

const saveSwipe = async (profileId) => {
  const response = await fetch("http://localhost:8080/matches", {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify({profileId})
  });

  if (!response.ok) {
    throw new Error('Failed to post data');
  } 
    
};

const sendMessage = async (conversationId,message) => {

  const requestBody = {
    msgTxt : message,
    authorId : "user"
  }

  const response = await fetch("http://localhost:8080/conversations/"+ conversationId, {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json'
    },
    body: JSON.stringify(requestBody)
  });

  if (!response.ok) {
    console.log(response)
    throw new Error('Failed to post data');
  } 

  return response.json();

}

const MatchesList = ( {matches, onSelectMatch}) => (
  <div className='rounded-lg shadow-lg p-4'>
  <h2 className='text-2xl font-bold mb-4'>Matches</h2>
  <ul>
    {matches.map((match, index) => (
      <li key={index} className='mb-2'>
        <button 
        className='w-full hover:bg-gray rounded flex item-center' 
        onClick={() => onSelectMatch(match.profile, match.conversationId)}>
          {/* Here i used http-server to use the project images  */}
         <img src={"http://127.0.0.1:8081/"+match.profile.imageUrl} className='w-16 h-16 rounded-full mr-3 object-corner'/> 
          <span>
            <h3 className='font-bold'>{match.profile.firstName} {console.log(matches)} {match.profile.lastName}</h3>
          </span>
          </button>
      </li>
    ))    
    }
  </ul>
  </div>
)

const ChatMessages = ({currentMatch, currentProfile, currentChat, refreshState, refreshMessages}) => {

  const [input, setInput] = useState('');
  const [temp, setTemp] = useState('');

  const handleSend = async () => {
    if(input.trim()){
    setTemp(input)
    setInput('')
    await sendMessage(currentChat.id, temp)
    }
    refreshState()
  }

  const printMessage = () => {
    let temp = inputUpdate
    return temp
  }

  return currentMatch ? (
    
    <div className='rounded-lg shadow-lg p-4'>
    <h2 className="text-2xl font-bold mb-4">Chat with {currentMatch.firstName} {currentMatch.lastName} </h2>
    <div className="h-[50vh] border rounded-lg overflow-y-auto mb-6 p-4 bg-gray-50">
      {currentChat.msgs.map((message, index) => (
        <div key={index} className={`flex ${message.authorId === 'user' ? 'justify-end' : 'justify-start'} mb-4`}>
          <div className={`flex items-end ${message.authorId === 'user' ? 'flex-row-reverse' : 'flex-row'}`}>
            {message.authorId === 'user' ? (<User size={15} />) : 
            (<img
              src={`http://localhost:8080/images/${currentProfile.imageUrl}`}
              className="w-11 h-11 rounded-full"
            />)}
            <div
              className={`px-4 py-2 rounded-2xl ${
                message.authorId === 'user'
                  ? 'bg-blue-500 text-white ml-2'
                  : 'bg-gray-200 text-gray-800 mr-2'
              }`}
            >
              {message.msgTxt}
            </div>
          </div>
        </div>
      ))}
    </div>
    <div className="flex items-center">
      <input
        type="text"
        value={input}
        onChange={(e) => setInput(e.target.value)}
        className="flex-1 border-2 border-gray-300 rounded-full py-2 px-4 mr-2 focus:outline-none focus:border-blue-500"
        placeholder="Type a message..."
      />
      <button
        className="bg-blue-500 text-white rounded-full p-2 hover:bg-blue-600 transition-colors duration-200"
        onClick={() => handleSend()}
      >
        <Send size={24} />
      </button>
    </div>
    </div>
    ) : (<div>Loading...</div>);
  }

const ProfileSelector = ({profile, onSwipe}) => {
  return (profile ? (
    <div className='rounded-lg overflow-hidden bg-white shadow-lg'>
      <div className='relative'>
       <img src={"http://127.0.0.1:8081/" + profile.imageUrl} alt="Profile" /> 
        <div className='absolute bottom-0 left-0 right-0 text-white p-4 bg-gradient-tp-r from black'>
          <h2 className='text-3xl font-bold'>{profile.firstName} {profile.lastName}</h2>
        </div>
      </div>
      <div className='p-4'>
        <p className='text-gray-600 mb-4'>{profile.bio}</p>
      </div>
      <diV className='p-4 flex justify-center space-x-4 text-white'>
      <button className='bg-red-500 rounded-full p-4 hover:bg-red-700' onClick={() => onSwipe(profile.id, "left")}>
        <X size={24}/>
        </button>
        <button className='bg-green-500 rounded-full p-4 text-white hover:bg-green-700' onClick={() => onSwipe(profile.id,"right")}>
        <Heart size={24}/>
      </button>
      </diV>
    </div>
   ) :   <div>Loading...</div>
  )
}

function App() {

  const loadRandomProfile = async () => {
    try{
      const profile = await fetchRandomProfile();
      setCurrentProfile(profile)
    }catch(error){
      console.log(error)
    }
  }

  const loadMatches = async () => {
    try{
      const matches = await fetchMatches();
      setCurrentMatches(matches)
    }catch(error){
      console.log(error)
    }
  }
  
  useEffect(() => {
    loadRandomProfile();
    loadMatches();
  }, [])

  const [currentScreen, setCurrentScreen] = useState('matches');
  const [currentProfile, setCurrentProfile] = useState(null);
  const [matches, setCurrentMatches] = useState([]);
  const [currentMatchAndChat, setCurrentMatchAndChat] = useState({ match: {}, conversation: []});

  const onSwipe = async (profileId, direction) => {
    loadRandomProfile();

    if(direction === "right"){
      await saveSwipe(profileId)
      await loadMatches();
    }
    
  }

  const refreshConversationState = async () => {
    const conversation = await fetchConversation(currentMatchAndChat.conversation.id)
    setCurrentMatchAndChat({match: currentMatchAndChat.match, conversation: conversation})
  }

  const refreshMessages = async () => {
    currentMatchAndChat.conversation.msgs.add("mmmmm")
    setCurrentMatchAndChat({match: currentMatchAndChat.match, conversation: currentMatchAndChat.conversation})
  }

  const onSelectMatch = async (profile, conversationId) => {
    const conversation = await fetchConversation(conversationId);
    //console.log(conversation.msgs)
   // const messages = conversation.msgs
    //console.log(Array.isArray(messages))
    setCurrentMatchAndChat({match: profile, conversation: conversation});
    console.log(currentMatchAndChat.match)
    console.log(Array.isArray(currentMatchAndChat.conversation))
    console.log(currentMatchAndChat.conversation)
    setCurrentScreen('messages');
  }

  const renderScreen = () => {
    switch(currentScreen){
      case 'profile':
        return <ProfileSelector profile={currentProfile} onSwipe={onSwipe}/>;
      case 'matches':
        return <MatchesList matches={matches} onSelectMatch={onSelectMatch}/>;
        case 'messages':
          return <ChatMessages currentMatch={currentMatchAndChat.match} currentProfile={currentProfile} currentChat={currentMatchAndChat.conversation} refreshState={refreshConversationState} refreshMessages={refreshMessages}/>;
    }
    
  }
  return (
      <div className='max-w-md mx-auto'>
        <nav className='flex justify-between'>
          <User onClick={() => setCurrentScreen('profile')}/>
          <MessageCircle onClick={() => setCurrentScreen('matches')}/>
        </nav>
        {renderScreen()}
      </div>
  );
}

export default App;
