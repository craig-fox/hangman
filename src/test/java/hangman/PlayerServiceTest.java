package hangman;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

public class PlayerServiceTest {
	private PlayerService service;
	private Player testPlayer;
	
	private List<Player> registry = new ArrayList<>();
	
	@Before
	public void init(){
		service = new PlayerService(registry);
		testPlayer = service.newPlayer("Neil");
	}
	
	private boolean isIdNew(long id){
		boolean isNew = service.getRegistry().stream().filter(p -> p.getId() == id).count() == 1L;
		return isNew;
	}
	
	@Test
	public void testAddNewPlayer(){
		Player player = service.newPlayer("Mary");
		assertTrue("Id must be unique " + player.getId(), isIdNew(player.getId()));
	}
	
	@Test
	public void testLoadRegistry(){
		Player[] players = {new Player("Craig"), new Player("Scott")};
		registry.addAll(Arrays.asList(players));
		service.setRegistry(registry);
		assertTrue("Registry should be number of players loaded in " + players.length + " " + service.getRegistry().size(), 
				service.getRegistry().size() == players.length);
	}
	
	@Test
	public void testGetPlayerById(){
		long id = testPlayer.getId();
		Player newPlayer =service.getPlayerById(id);
		assertEquals("Should return same player", testPlayer, newPlayer);
		
	}
	
	@Test
	public void testGetPlayerByName(){
		String name = testPlayer.getName();
		Player newPlayer =service.getPlayer(name);
		assertEquals("Should return same player", testPlayer, newPlayer);
		
	}
	
	@Test
	public void testCheckPlayerInRegistry(){
		assertTrue("Player should be in registry", service.inRegistry(testPlayer.getName()));
	}
	
	@Test
	public void testCheckPlayerNotInRegistry(){
		assertFalse("Player should not be in registry", service.inRegistry("Fred"));
	}

}

