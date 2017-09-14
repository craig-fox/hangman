package hangman;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class GameService {
	
	public static final int maxRemainingGuesses = 8;
	public static final int alphabet_size = 26;
	private static final int ascii_a = 97;
	private static final Set<Character> ALPHABET = new LinkedHashSet<>();
	private static final String[] words = {"serpent", "tapioca", "jazz", "derringer", "foible", "vexing", "rinderpest", "bruit", "gyp", "madrigal"};
	private Random random;
	private final AtomicLong gameId = new AtomicLong();
	
	static {
		for(int i =0; i < alphabet_size; i++){
			ALPHABET.add((char)(ascii_a + i));
		}
	}
	
	public static class Messages {
		public static final String invalid_letter = "This guess is not a valid letter";
		public static final String not_available = "Letter is no longer available for guessing";
		public static final String already_found = "This letter has already been found in the word";
		public static final String won = "You won!";
		public static final String lost = "No more guesses. You lost!";
		public static final String correct_guess = "You guessed correctly!";
		public static final String wrong_guess = "Your guess was wrong";
	}
	
	public static Set<Character> getAlphabet(){
		return new LinkedHashSet<Character>(ALPHABET);
	}
	
	private String selectWord(){
		String word = words[this.random.nextInt(words.length)];
		return word;
	}
	
	private List<String> initWordInProgress(int size){	
		List<String> result = new ArrayList<String>();
		for(int i=0; i < size; i++ ){
			result.add("");
		}
		
		return result;
	}
	
	private boolean isLetterAvailable(Game game, char letter){
		return game.getRemainingLetters().contains(letter);
	}
	
	private boolean isLetterAlreadyFound(Game game, char letter){
		String letterAsString = Character.toString(letter);
		return game.getWordInProgress().contains(letterAsString);
	}
	
	public Game processGuess(Game game, String guess){
		Game result = game;
		char letter =  guess.charAt(0);
		boolean isViable = Character.isLetter(letter);
		
		if(!isViable){
			result.setMessage(Messages.invalid_letter);
			return result;
		}
		
		letter = guess.toLowerCase().charAt(0);
		if(!isLetterAvailable(result, letter) && !(isLetterAlreadyFound(result, letter))){
			result.setMessage(Messages.not_available);
			return result;
		} else if (isLetterAlreadyFound(result, letter)) {
			result.setMessage(Messages.already_found);
			return result;
		}
		
		String letterAsString = Character.toString(letter);
		boolean hasFoundLetter = game.getTargetWord().contains(letterAsString);
		if(hasFoundLetter){
			String word = game.getTargetWord();
			int index = word.indexOf(letterAsString);
			while (index >= 0) {
				game.getWordInProgress().set(index, letterAsString);
			    index = word.indexOf(guess, index + 1);
			}
			
			if(result.getDisplayProgress().equals(result.getTargetWord())){
				result.setMessage(Messages.won);
				return result;
			} else {
				result.setMessage(Messages.correct_guess);
			}
				
			
		} else {
			result.setRemainingGuesses(result.getRemainingGuesses() -1);
			if(result.getRemainingGuesses() == 0){
				result.setMessage(Messages.lost);
			} else {
				result.setMessage(Messages.wrong_guess);
			}
		}
		result.getRemainingLetters().remove(letter);
		
	
		
		return result;
	}
	
	public Game launchGame(){
	  this.random = new Random();
	  Game game = new Game();
	  game.setId(gameId.incrementAndGet());
	  game.setRemainingGuesses(maxRemainingGuesses);
	  Set<Character> alphabet = new LinkedHashSet<>();
	  alphabet.addAll(getAlphabet());
	  game.setRemainingLetters(alphabet);
	  game.setTargetWord(selectWord());
	  game.setWordInProgress(this.initWordInProgress(game.getTargetWord().length()));
	  game.setMessage("Game has started");
	 
	  return game;
	}

}
