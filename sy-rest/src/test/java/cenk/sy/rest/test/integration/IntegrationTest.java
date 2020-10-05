package cenk.sy.rest.test.integration;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;

import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import cenk.sy.backend.pojo.SyContractValueResponse;
import cenk.sy.jpa.common.SyConstants;
import cenk.sy.jpa.entity.contract.SyContract;
import cenk.sy.jpa.entity.player.SyPlayer;
import cenk.sy.jpa.entity.team.SyCurrency;
import cenk.sy.jpa.entity.team.SyTeam;
import cenk.sy.jpa.repo.SyPlayerRepo;
import cenk.sy.jpa.repo.SyTeamRepo;
import cenk.sy.rest.SyRestApi;
import cenk.sy.rest.pojo.SyErrorResponse;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = SyRestApi.class)
@AutoConfigureMockMvc
@PropertySource("classpath:test.properties") 
public class IntegrationTest implements SyConstants {
	private static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));
	private static final SyCurrency C_EUR = new SyCurrency(33l);
	private static final SyCurrency C_GBP = new SyCurrency(1l);
	private static final SyTeam T_BARCA = new SyTeam("barcelona", C_EUR);
	private static final SyPlayer P_MARADONA = new SyPlayer("diego armando maradona franco", 1960);
	
	@Autowired
	private MockMvc mockMvc;
	
	@Autowired
	private SyTeamRepo tRepo;
	@Autowired
	private SyPlayerRepo pRepo;
	
	private void clearTables() {
		pRepo.deleteAll();
		tRepo.deleteAll();
	}
	
	@Before
	public void init() {
		clearTables();
	}
	
	@Test
	public void createUpdateDeleteTeam() throws Exception{
		String requestJson = JACKSON_WRITER.writeValueAsString(T_BARCA);
		MvcResult result = this.mockMvc
				.perform(post("/jpa/team")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(201,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		SyTeam team = JACKSON_MAPPER.readValue(respString, SyTeam.class);
		assertEquals("Barcelona", team.getName());
		assertEquals("EUR", team.getCurrency().getCode());
		
		
		team.setName("chelsea");
		team.setCurrency(C_GBP);
		Long id = team.getId();
		team.setId(null);
		String requestJson2 = JACKSON_WRITER.writeValueAsString(team);
		MvcResult result2 = this.mockMvc
				.perform(put("/jpa/team/" + id)
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson2))
				.andReturn();
		assert(result2!=null);
		MockHttpServletResponse response2 = result2.getResponse();
		assert(response2!=null);
		assertEquals(200,response2.getStatus());
		String respString2 = response2.getContentAsString();
		assert(respString2!=null);
		team = JACKSON_MAPPER.readValue(respString2, SyTeam.class);
		assertEquals("Chelsea", team.getName());
		assertEquals("GBP", team.getCurrency().getCode());
		
		
		MvcResult result3 = this.mockMvc
				.perform(delete("/jpa/team/" + id))
				.andReturn();
		assert(result3!=null);
		MockHttpServletResponse response3 = result3.getResponse();
		assert(response3!=null);
		assertEquals(204,response3.getStatus());
		String respString3 = response3.getContentAsString();
		assert(respString3!=null);
		assertEquals("", respString3);
	}
	
	@Test
	public void createUpdateDeletePlayer() throws Exception{
		String requestJson = JACKSON_WRITER.writeValueAsString(P_MARADONA);
		MvcResult result = this.mockMvc
				.perform(post("/jpa/player")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(201,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		SyPlayer savedPlayer = JACKSON_MAPPER.readValue(respString, SyPlayer.class);
		assertEquals("Diego Armando Maradona Franco", savedPlayer.getName());
		assert(1960 == savedPlayer.getBirthYear());
		
		SyPlayer playerToUpdate = new SyPlayer();
		playerToUpdate.setName("kylian sanmi mbappe lottin");
		playerToUpdate.setBirthYear(1998);
		Long id = savedPlayer.getId();
		String requestJson2 = JACKSON_WRITER.writeValueAsString(playerToUpdate);
		MvcResult result2 = this.mockMvc
				.perform(put("/jpa/player/" + id)
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson2))
				.andReturn();
		assert(result2!=null);
		MockHttpServletResponse response2 = result2.getResponse();
		assert(response2!=null);
		assertEquals(200,response2.getStatus());
		String respString2 = response2.getContentAsString();
		assert(respString2!=null);
		playerToUpdate = JACKSON_MAPPER.readValue(respString2, SyPlayer.class);
		assertEquals("Kylian Sanmi Mbappe Lottin", playerToUpdate.getName());
		assert(1998 == playerToUpdate.getBirthYear());
		
		
		MvcResult result3 = this.mockMvc
				.perform(delete("/jpa/player/" + id))
				.andReturn();
		assert(result3!=null);
		MockHttpServletResponse response3 = result3.getResponse();
		assert(response3!=null);
		assertEquals(204,response3.getStatus());
		String respString3 = response3.getContentAsString();
		assert(respString3!=null);
		assertEquals("", respString3);
	}

	@Test
	public void addContractAndGetValue() throws Exception{
		SyPlayer player = new SyPlayer();
		player.setName("kylian sanmi mbappe lottin");
		player.setBirthYear(1998);
		String requestJson1 = JACKSON_WRITER.writeValueAsString(player);
		MvcResult result1 = this.mockMvc
				.perform(post("/jpa/player")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson1))
				.andReturn();
		assert(result1!=null);
		MockHttpServletResponse response1 = result1.getResponse();
		assert(response1!=null);
		assertEquals(201,response1.getStatus());
		String respString1 = response1.getContentAsString();
		assert(respString1!=null);
		player = JACKSON_MAPPER.readValue(respString1, SyPlayer.class);
		Long playerId = player.getId();
		assertEquals("Kylian Sanmi Mbappe Lottin", player.getName());
		assert(1998 == player.getBirthYear());
		
		
		SyTeam team = new SyTeam();
		team.setName("paris saint - germain");
		team.setCurrency(C_EUR);
		String requestJson2 = JACKSON_WRITER.writeValueAsString(team);
		MvcResult result2 = this.mockMvc
				.perform(post("/jpa/team")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson2))
				.andReturn();
		assert(result2!=null);
		MockHttpServletResponse response2 = result2.getResponse();
		assert(response2!=null);
		assertEquals(201,response2.getStatus());
		String respString2 = response2.getContentAsString();
		assert(respString2!=null);
		team = JACKSON_MAPPER.readValue(respString2, SyTeam.class);

		Long teamId = team.getId();
		team.setId(null);
		assertEquals("Paris Saint - Germain", team.getName());
		assertEquals("EUR", team.getCurrency().getCode());
		
		SyPlayer playerToUpdate = new SyPlayer();
		List<SyContract> contracts = new ArrayList<>();
		SyTeam teamForContract = new SyTeam();
		teamForContract.setId(teamId);
		for(int i = 0; i < 7; i++) {
			SyContract c = new SyContract();
			c.setTeam(teamForContract);
			c.setYear(2020-i);
			contracts.add(c);
		}
		playerToUpdate.setContracts(contracts);
		
		
		String requestJson3 = JACKSON_WRITER.writeValueAsString(playerToUpdate);
		MvcResult result3 = this.mockMvc
				.perform(put("/jpa/player/" + playerId)
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson3))
				.andReturn();
		assert(result3!=null);
		MockHttpServletResponse response3 = result3.getResponse();
		assert(response3!=null);
		assertEquals(200,response3.getStatus());
		String respString3 = response3.getContentAsString();
		assert(respString3!=null);
		player = JACKSON_MAPPER.readValue(respString3, SyPlayer.class);
		assertEquals("Kylian Sanmi Mbappe Lottin", player.getName());
		assert(1998 == player.getBirthYear());
		
		
		MvcResult result4 = this.mockMvc
				.perform(get("/api/contractValue?playerId=" + playerId))
				.andReturn();
		assert(result4!=null);
		MockHttpServletResponse response4 = result4.getResponse();
		assert(response4!=null);
		assertEquals(200,response4.getStatus());
		String respString4 = response4.getContentAsString();
		assert(respString4!=null);
		SyContractValueResponse resp = JACKSON_MAPPER.readValue(respString4, SyContractValueResponse.class);
		assertEquals("EUR", resp.getCurrencyCode());
		assert(360000f == resp.getValue());
		
	}

	@Test
	public void createPlayerWithError() throws Exception{
		SyPlayer player = new SyPlayer();
		player.setName("kylian sanmi mbappe lottin");
		player.setBirthYear(1898);
		String requestJson1 = JACKSON_WRITER.writeValueAsString(player);
		MvcResult result1 = this.mockMvc
				.perform(post("/jpa/player")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson1))
				.andReturn();
		assert(result1!=null);
		MockHttpServletResponse response1 = result1.getResponse();
		assert(response1!=null);
		assertEquals(500,response1.getStatus());
		String respString1 = response1.getContentAsString();
		assert(respString1!=null);
		SyErrorResponse resp1 = JACKSON_MAPPER.readValue(respString1, SyErrorResponse.class);
		assertEquals(1003, resp1.getErrorCode());
		
		
		player.setBirthYear(2006);
		String requestJson2 = JACKSON_WRITER.writeValueAsString(player);
		MvcResult result2 = this.mockMvc
				.perform(post("/jpa/player")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson2))
				.andReturn();
		assert(result2!=null);
		MockHttpServletResponse response2 = result2.getResponse();
		assert(response2!=null);
		assertEquals(500,response2.getStatus());
		String respString2 = response2.getContentAsString();
		assert(respString2!=null);
		SyErrorResponse resp2 = JACKSON_MAPPER.readValue(respString2, SyErrorResponse.class);
		assertEquals(1003, resp2.getErrorCode());
		
		
		player.setBirthYear(1998);
		player.setName("   ");
		String requestJson3 = JACKSON_WRITER.writeValueAsString(player);
		MvcResult result3 = this.mockMvc
				.perform(post("/jpa/player")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson3))
				.andReturn();
		assert(result3!=null);
		MockHttpServletResponse response3 = result3.getResponse();
		assert(response3!=null);
		assertEquals(500,response3.getStatus());
		String respString3 = response3.getContentAsString();
		assert(respString3!=null);
		SyErrorResponse resp3 = JACKSON_MAPPER.readValue(respString3, SyErrorResponse.class);
		assertEquals(1003, resp3.getErrorCode());
	}
}
