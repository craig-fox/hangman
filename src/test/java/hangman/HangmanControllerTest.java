package hangman;

import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.http.MockHttpOutputMessage;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;

import static org.hamcrest.Matchers.*;
import static org.junit.Assert.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Arrays;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@WebAppConfiguration
public class HangmanControllerTest {
	
	private MockMvc mockMvc;
	private HttpMessageConverter mappingJackson2HttpMessageConverter;
	private MediaType contentType = new MediaType(MediaType.APPLICATION_JSON.getType(),
            MediaType.APPLICATION_JSON.getSubtype(),
            Charset.forName("utf8"));
	private PlayerService playerService;
	private GameService gameService;
	private HangmanController controller;
	
	@Autowired
    private WebApplicationContext webApplicationContext;
	
	
	@Autowired
    void setConverters(HttpMessageConverter<?>[] converters) {

        this.mappingJackson2HttpMessageConverter = Arrays.asList(converters).stream()
            .filter(hmc -> hmc instanceof MappingJackson2HttpMessageConverter)
            .findAny()
            .orElse(null);

        assertNotNull("the JSON message converter must not be null",
                this.mappingJackson2HttpMessageConverter);
    }
	
	@Before
    public void setup() throws Exception {
        this.mockMvc = webAppContextSetup(webApplicationContext).build();
        this.playerService.newPlayer("Abel");
        this.playerService.newPlayer("Cain");
    }
	
	@Test
    public void listPlayers() throws Exception {
		System.out.println(this.json(this.playerService.getRegistry()));
		
		mockMvc.perform(get("/players/")
                .content(this.json(this.playerService.getRegistry()))
                .contentType(contentType))
                .andExpect(status().isOk());	
	}
	
	@Test
	public void registerPlayerAsGuest() throws Exception {
		mockMvc.perform(get("/register/")
			   .content(this.json(this.playerService.getPlayer("guest")))
			   .contentType(contentType))
		       .andExpect(status().isOk())
		       ;
	}
	
	@Test
	public void registerPlayerWithName() throws Exception {
		mockMvc.perform(get("/register/?player=sam")
			   .content(this.json(this.playerService.getPlayer("sam")))
			   .contentType(contentType))
		       .andExpect(status().isOk())
		       ;
	}
	
	@Test
	public void launchGame() throws Exception {
		Player player = this.playerService.newPlayer("sue");
		long playerId = player.getId();
		Game game = this.gameService.launchGame();
		player.setGame(game);
		
		mockMvc.perform(get("/player/" + playerId + "/game/")
		   .content(this.json(player))
		   .contentType(contentType))
	       .andExpect(status().isOk())
	       ;
	}
	
	@Test
	public void makeGuess() throws Exception {
		Player player = this.playerService.newPlayer("bob");
		long playerId = player.getId();
		Game game = this.gameService.launchGame();
		player.setGame(game);
		String word = game.getTargetWord();
		
		mockMvc.perform(get("/player/" + playerId + "/game/b")
		   .content(this.json(player))
		   .contentType(contentType))
	       .andExpect(status().isOk())
	       ;
	}
	
	protected String json(Object o) throws IOException {
        MockHttpOutputMessage mockHttpOutputMessage = new MockHttpOutputMessage();
        this.mappingJackson2HttpMessageConverter.write(
                o, MediaType.APPLICATION_JSON, mockHttpOutputMessage);
        return mockHttpOutputMessage.getBodyAsString();
    }

}
