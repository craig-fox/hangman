package hangman;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

@Component
public class PlayerService {
	private final AtomicLong playerId = new AtomicLong();
	private List<Player> registry = new ArrayList<>();
	
	private long getNewId(){
		long highest = 0L;
		if(!registry.isEmpty()){
			highest = getRegistry().stream().mapToLong(p -> p.getId()).max().getAsLong();
		}
		
		this.playerId.set(highest);					
		return this.playerId.incrementAndGet();		
	}
   
	public List<Player> getRegistry(){
		return registry;
	}
	
	public void setRegistry(List<Player> registry){
		this.registry.clear();
		for(Player player: registry){
			if(player.getId() > 0){
				this.registry.add(player);
			} else {
				newPlayer(player.getName());
			}
			
		}
	}
	
	public boolean inRegistry(String name){
		boolean result = false;
		for(Player player: getRegistry()){
			if(player.getName().equals(name)){
			    result = true;
			    break;
			}
		}
		return result;
	}
	
	public PlayerService(){
		
	}
	
	public PlayerService(List<Player> registry){
		setRegistry(registry);
	} 
	
	public Player getPlayer(String name){
		Player result = null;
		for(Player player: getRegistry()){
			if(player.getName().equals(name)){
			    result = player;
			    break;
			}
		}
		return result;
	}
	
	public Player getPlayerById(long id){
		Player result = null;
		for(Player player: getRegistry()){
			if(player.getId() == id){
			    result = player;
			    break;
			}
		}
		return result;
	}
	
	public Player newPlayer(String name){
		Player player = new Player(name);
		player.setId(getNewId());
		this.registry.add(player);
		return player;
	}
}
