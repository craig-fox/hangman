package hangman;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class Game implements Serializable {
	private long id;
	private long playerId;
    private String targetWord;
    private List<String> wordInProgress;
    private int remainingGuesses;
    private Set<Character> remainingLetters;
    private String displayProgress = "";
    private String message = "";
    
    public String getDisplayProgress(){
    	StringBuilder result = new StringBuilder();
    	for(String letter: wordInProgress){
    		if(letter.isEmpty()){
    			result.append(" _ ");
    		} else {
    			result.append(letter);
    		}
    	}
    	
    	return result.toString().trim();
    }

  

    public long getId() {
        return id;
    }


	public List<String> getWordInProgress() {
		return wordInProgress;
	}


	public void setWordInProgress(List<String> wordInProgress) {
		this.wordInProgress = wordInProgress;
	}


	public int getRemainingGuesses() {
		return remainingGuesses;
	}


	public void setRemainingGuesses(int remainingGuesses) {
		this.remainingGuesses = remainingGuesses;
	}


	public Set<Character> getRemainingLetters() {
		return remainingLetters;
	}


	public void setRemainingLetters(Set<Character> remainingLetters) {
		if(this.remainingLetters != null){
			this.remainingLetters.clear();
			this.remainingLetters.addAll(remainingLetters);
		} else {
			this.remainingLetters = remainingLetters;
		}
		
	}


	public void setId(long id) {
		this.id = id;
	}


	public String getTargetWord() {
		return targetWord;
	}


	public void setTargetWord(String targetWord) {
		this.targetWord = targetWord;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public long getPlayerId() {
		return playerId;
	}

	public void setPlayerId(long playerId) {
		this.playerId = playerId;
	}
	
	

    
    
    
}
