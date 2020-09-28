package cenk.sy.rest.test.integration;

import static org.junit.Assert.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;

import java.nio.charset.Charset;
import java.time.LocalDate;

import org.json.JSONArray;
import org.json.JSONObject;
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

import cenk.ozan.backend.pojo.OzConvertionListRequest;
import cenk.ozan.backend.pojo.OzConvertionResponse;
import cenk.ozan.jpa.common.OzConstants;
import cenk.ozan.jpa.entity.rate.OzRate;
import cenk.sy.rest.OzRestApi;
import cenk.sy.rest.pojo.OzErrorResponse;


@RunWith(SpringRunner.class)
@SpringBootTest(classes = OzRestApi.class)
@AutoConfigureMockMvc
@PropertySource("classpath:application.properties") 
public class IntegrationTest implements OzConstants{
	public static final MediaType APPLICATION_JSON_UTF8 = new MediaType(MediaType.APPLICATION_JSON.getType(), MediaType.APPLICATION_JSON.getSubtype(), Charset.forName("utf8"));

	@Autowired
	private MockMvc mockMvc;
	
	@Test
	public void getRate() throws Exception{
		MvcResult result = this.mockMvc
				.perform(get("/api/rate?from=USD&to=TRY")).andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(200,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		OzRate rate = JACKSON_MAPPER.readValue(respString, OzRate.class);
		assert(rate.getRate()>0f);
	}

	@Test
	public void getRateErrorWrongCurrencyCode() throws Exception{
		MvcResult result = this.mockMvc
				.perform(get("/api/rate?from=USD&to=XXX")).andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(500,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		OzErrorResponse error = JACKSON_MAPPER.readValue(respString, OzErrorResponse.class);
		assert(error.getErrorCode()==1001);
	}

	@Test
	public void convert() throws Exception{
		MvcResult result = this.mockMvc
				.perform(get("/api/convert?from=GBP&to=TRY&amount=5000")).andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(200,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		OzConvertionResponse conv = JACKSON_MAPPER.readValue(respString, OzConvertionResponse.class);
		assert(conv.getTargetAmount()>0f);
		assert(conv.getTransactionId()>0L);
	}

	@Test
	public void convertErrorMissingParam() throws Exception{
		MvcResult result = this.mockMvc
				.perform(get("/api/convert?from=DKK&amount=3000")).andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(400,response.getStatus());
		String errString = response.getErrorMessage();
		assertEquals("Required String parameter 'to' is not present", errString);
	}

	@Test
	public void list() throws Exception{
		MvcResult result = this.mockMvc
				.perform(get("/api/convert?from=CAD&to=DKK&amount=2500")).andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(200,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		OzConvertionResponse conv = JACKSON_MAPPER.readValue(respString, OzConvertionResponse.class);
		assert(conv.getTargetAmount()>0f);
		assert(conv.getTransactionId()>0L);

		LocalDate today = LocalDate.now();
		OzConvertionListRequest req = new OzConvertionListRequest();
		req.setTransactionDate(today.toString());
		req.setTransactionId(conv.getTransactionId());
		String requestJson = JACKSON_WRITER.writeValueAsString(req);
		MvcResult result2 = this.mockMvc
				.perform(post("/api/list")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andReturn();
		assert(result2!=null);
		MockHttpServletResponse response2 = result2.getResponse();
		assert(response2!=null);
		assertEquals(200,response2.getStatus());
		String respString2 = response2.getContentAsString();
		assert(respString2!=null);
		JSONArray jArray = new JSONArray(respString2);
		assert(jArray!=null);
		assertEquals(1, jArray.length());
		JSONObject jObject = jArray.getJSONObject(0);
		double tAmount = jObject.getDouble("targetAmount");
		assert(tAmount>0D);

	}

	@Test
	public void listErrorMissingPost() throws Exception{
		MvcResult result = this.mockMvc
				.perform(get("/api/convert?from=CAD&to=DKK&amount=2500")).andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(200,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		OzConvertionResponse conv = JACKSON_MAPPER.readValue(respString, OzConvertionResponse.class);
		assert(conv.getTargetAmount()>0f);
		assert(conv.getTransactionId()>0L);
		OzConvertionListRequest req = new OzConvertionListRequest();
		String requestJson = JACKSON_WRITER.writeValueAsString(req);
		MvcResult result2 = this.mockMvc
				.perform(post("/api/list")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andReturn();
		assert(result2!=null);
		MockHttpServletResponse response2 = result2.getResponse();
		assert(response2!=null);
		assertEquals(500,response2.getStatus());
		String respString2 = response2.getContentAsString();
		assert(respString2!=null);
		OzErrorResponse error = JACKSON_MAPPER.readValue(respString2, OzErrorResponse.class);
		assertEquals(1004, error.getErrorCode());
	}

	@Test
	public void listErrorWrongPost() throws Exception{
		MvcResult result = this.mockMvc
				.perform(get("/api/convert?from=CAD&to=DKK&amount=2500")).andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(200,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		OzConvertionResponse conv = JACKSON_MAPPER.readValue(respString, OzConvertionResponse.class);
		assert(conv.getTargetAmount()>0f);
		assert(conv.getTransactionId()>0L);

		LocalDate today = LocalDate.now();
		String requestJson = JACKSON_WRITER.writeValueAsString(today);
		MvcResult result2 = this.mockMvc
				.perform(post("/api/list")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andReturn();
		assert(result2!=null);
		MockHttpServletResponse response2 = result2.getResponse();
		assert(response2!=null);
		
		assertEquals(500,response2.getStatus());
		String respString2 = response2.getContentAsString();
		assert(respString2!=null);
		OzErrorResponse error = JACKSON_MAPPER.readValue(respString2, OzErrorResponse.class);
		assertEquals(1004, error.getErrorCode());
	}

	@Test
	public void listByDate() throws Exception{
		MvcResult result = this.mockMvc
				.perform(get("/api/convert?from=CAD&to=DKK&amount=2500")).andReturn();
		assert(result!=null);
		MockHttpServletResponse response = result.getResponse();
		assert(response!=null);
		assertEquals(200,response.getStatus());
		String respString = response.getContentAsString();
		assert(respString!=null);
		OzConvertionResponse conv = JACKSON_MAPPER.readValue(respString, OzConvertionResponse.class);
		assert(conv.getTargetAmount()>0f);
		assert(conv.getTransactionId()>0L);

		MvcResult result2 = this.mockMvc
				.perform(get("/api/convert?from=CAD&to=DKK&amount=2500")).andReturn();
		assert(result2!=null);
		MockHttpServletResponse response2 = result2.getResponse();
		assert(response2!=null);
		assertEquals(200,response2.getStatus());
		String respString2 = response2.getContentAsString();
		assert(respString2!=null);
		OzConvertionResponse conv2 = JACKSON_MAPPER.readValue(respString2, OzConvertionResponse.class);
		assert(conv2.getTargetAmount()>0f);
		assert(conv2.getTransactionId()>0L);

		LocalDate today = LocalDate.now();
		OzConvertionListRequest req = new OzConvertionListRequest();
		req.setTransactionDate(today.toString());
		String requestJson = JACKSON_WRITER.writeValueAsString(req);
		MvcResult result3 = this.mockMvc
				.perform(post("/api/list")
						.contentType(APPLICATION_JSON_UTF8)
						.content(requestJson))
				.andReturn();
		assert(result3!=null);
		MockHttpServletResponse response3 = result3.getResponse();
		assert(response3!=null);
		assertEquals(200,response3.getStatus());
		String respString3 = response3.getContentAsString();
		assert(respString3!=null);
		JSONArray jArray = new JSONArray(respString3);
		assert(jArray!=null);
		int len = jArray.length();
		assert(len>1);
		JSONObject jObject = jArray.getJSONObject(0);
		double tAmount = jObject.getDouble("targetAmount");
		assert(tAmount>0D);
	}
}
