package hangman;

import java.io.Serializable;

public class Player implements Serializable {
    private long id;
    private String name;
    private Game game;
    
    public Player(String name){
    	this.name = name;
    }
    
	public long getId() {
		return id;
	}
	public void setId(long l) {
		this.id = l;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Game getGame() {
		return game;
	}
	public void setGame(Game game) {
		this.game = game;
	}
  
  
}
