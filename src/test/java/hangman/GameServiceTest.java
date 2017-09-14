package hangman;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class GameServiceTest {
	
  private GameService service;
  private Game testGame;
  private int originalGuesses;
  

  @Before
  public void init(){
	 service = new GameService(); 
	 String targetWord = "sausage";
	 List<String> blanks = new ArrayList<String>();
	 
	 testGame = new Game();
	 testGame.setId(1);
	 testGame.setPlayerId(1);
	 testGame.setRemainingGuesses(GameService.maxRemainingGuesses);
	 testGame.setTargetWord(targetWord);
	 testGame.setRemainingLetters(GameService.getAlphabet());
	 
	 for(int i=0; i < targetWord.length(); i++ ){
		blanks.add(""); 
	 }
	 
	 testGame.setWordInProgress(blanks);
	 originalGuesses = testGame.getRemainingGuesses();
  }
	
  @Test	
  public void testLaunchGame(){
	  Game game = service.launchGame();
	  assertTrue("Should have full alphabet " + game.getRemainingLetters().size(), game.getRemainingLetters().size() == GameService.alphabet_size);
	  assertTrue("Should have maximum guesses", game.getRemainingGuesses() == originalGuesses);	  
  }
  
  @Test
  public void testProcessGuessNotLetter(){
	  Game game = service.processGuess(testGame, "1");
	  assertTrue("Should only accept alphabetic characters", game.getMessage().equals(GameService.Messages.invalid_letter));  
  }
  
  @Test
  public void testProcessGuessRightLetter(){
	  Game game = service.processGuess(testGame, "u"); 
	  assertTrue("Should be a \'u\' in third place", game.getWordInProgress().get(2).equals("u"));
	  assertFalse("\'u\' should be unavailable", game.getRemainingLetters().contains('u'));
	  assertTrue("Should be unchanged number of guesses remaining", game.getRemainingGuesses() == originalGuesses);
  }
  
  @Test
  public void testProcessGuessRightMultiple(){
	  Game game = service.processGuess(testGame, "s"); 
	  assertTrue("First letter should be \'s\'", game.getWordInProgress().get(0).equals("s"));
	  assertTrue("First letter should be \'s\'", game.getWordInProgress().get(3).equals("s"));
	  assertFalse("\'s\' should be unavailable",game.getRemainingLetters().contains('s'));
	  assertTrue("Should be unchanged number of guesses remaining", game.getRemainingGuesses() == originalGuesses);
  }
  
  @Test
  public void testProcessGuessWrongLetter(){
	  Game game = service.processGuess(testGame, "x"); 
	  assertFalse("Word in progress should not contain x", game.getWordInProgress().contains("x"));
	  assertFalse("Remaining letters should not contain x", game.getRemainingLetters().contains('x'));
	  assertTrue("Should be one fewer remaining guess ", 
			  game.getRemainingGuesses() == originalGuesses-1);
  }
  
  @Test
  public void testProcessGuessUnavailableLetter(){
	  Game game = service.processGuess(testGame, "x"); 
	  game = service.processGuess(game, "x"); 
	  assertTrue("Letter should be unavailable for selection", game.getMessage().equals(GameService.Messages.not_available)); 
  }
  
  @Test
  public void testProcessGuessAlreadyInWord(){
	  Game game = service.processGuess(testGame, "u"); 
	  game = service.processGuess(game, "u"); 
	  assertTrue("Letter should be already found", game.getMessage().equals(GameService.Messages.already_found)); 
  }
  
  @Test
  public void testLoseGame(){
	  Game game = service.processGuess(testGame, "b"); 
	  game = service.processGuess(game, "c"); 
	  game = service.processGuess(game, "d"); 
	  game = service.processGuess(game, "f"); 
	  game = service.processGuess(game, "h"); 
	  game = service.processGuess(game, "i"); 
	  game = service.processGuess(game, "j"); 
	  game = service.processGuess(game, "k"); 
	  
	  assertTrue("Should be no guesses left", game.getRemainingGuesses() == 0);
	  assertTrue("Game should be lost", game.getMessage().equals(GameService.Messages.lost)); 
  }
  
  @Test
  public void testWinGame(){
	  Game game = service.processGuess(testGame, "s"); 
	  game = service.processGuess(game, "a"); 
	  game = service.processGuess(game, "u"); 
	  game = service.processGuess(game, "g"); 
	  game = service.processGuess(game, "e"); 
	  
	  assertTrue("Word in progress should match target", game.getDisplayProgress().equals(testGame.getTargetWord()));
	  assertTrue("Game should be won", game.getMessage().equals(GameService.Messages.won));
  }
}
