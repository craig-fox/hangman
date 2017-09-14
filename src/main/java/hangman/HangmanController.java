package hangman;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HangmanController {
	@Autowired
    private GameService gameService;
	
	//@Autowired
    private PlayerService playerService;
    
    @Autowired
    private HttpSession session;
    
    private void initializeSession(){
    	if(session.isNew() || session.getAttribute("players") == null){
    	    session.setAttribute("players", new ArrayList<Player>());
	  	}
    	
    	if(playerService == null){
    		playerService = new PlayerService((List<Player>)session.getAttribute("players"));	
    	}	
    }
    
    private void updateSession(){
    	session.setAttribute("players", playerService.getRegistry());
    }
    
    @RequestMapping(method = RequestMethod.GET, value="/players")
    public List<Player> players(){
    	initializeSession();
    	return playerService.getRegistry();
    }
     
    @RequestMapping(method = RequestMethod.GET, value="/register")
    public Player register (@RequestParam(value="player", defaultValue="guest") String playerName){
      initializeSession();
      Player player = null;
      
  	  if(!playerService.inRegistry(playerName)){
  		 player = playerService.newPlayer(playerName);
  	  } else {
  		 player = playerService.getPlayer(playerName);
  	  }
  	  
  	  updateSession();
      return player;
    }
    
    @RequestMapping(method = RequestMethod.GET, value="player/{playerId}/game")
    public Game launch(@PathVariable String playerId, @RequestParam(value="reset", defaultValue="false") String reset){
    	initializeSession();
    	Player player = playerService.getPlayerById( Long.parseLong(playerId));
    	Game game = null;
    	
        if(player != null){
        	
    		if(player.getGame() != null && !reset.toLowerCase().equals(Boolean.toString(true))){
        		game = player.getGame();
        		game.setMessage("Game still in progress");
        	} else {
        		
        		game = gameService.launchGame();
            	player.setGame(game);
            	game.setPlayerId(player.getId());
        	}
    		
    	} else {
    		game = new Game();
    		game.setId(0L);
    		game.setPlayerId(0L);
    		game.setRemainingGuesses(0);
    		game.setRemainingLetters(null);
    		game.setTargetWord("");
    		game.setWordInProgress(new ArrayList<String>());
    		game.setMessage("Player ID not found");
    	} 
        updateSession();
    	return game;
    }
    
    
    @RequestMapping(method = RequestMethod.GET, value="player/{playerId}/game/{guess}")
    public Game game(@PathVariable String playerId, @PathVariable String guess){
    	initializeSession();
    	Player player = playerService.getPlayerById( Long.parseLong(playerId));
    	Game game = player.getGame();
    	if(guess.length() != 1){
    		game.setMessage("Guess must be a single letter");
    	} else {
    		game = gameService.processGuess(game, guess);
    	}
    	updateSession();
    	return game; 	
    }
      
}
