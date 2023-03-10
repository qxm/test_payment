package com.qxm.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerIntegrationTest {

	@LocalServerPort
	private int port;

	TestRestTemplate restTemplate = new TestRestTemplate();
	// Required to Generate JSON content from Java objects
	public static final ObjectMapper objectMapper = new ObjectMapper();

	private String createURLWithPort(String uri) {

		return "http://localhost:" + port + uri;

	}
	
	@Before
	public void setup() {
		restTemplate.getRestTemplate().setRequestFactory(new HttpComponentsClientHttpRequestFactory());
	}
	
	/**
	 * Test the GET /v1/payment/login/{name} API
	 */
	@Test
	public void testPayment() throws JsonProcessingException{
		// API call
		Map<String, Object> response = restTemplate.getForObject("http://localhost:" + port + "/v1/payment/login/jack",
				Map.class);

		assertNotNull(response);

		// Asserting API Response
		Long id = (Long) response.get("id");
		assertNotNull(id);
		
		String name = (String) response.get("name");
		assertNotNull(name);
		assertEquals("jack", name);
		
		BigDecimal balance = new BigDecimal ((String)response.get("balance"));
		assertNotNull(balance);
		assertEquals(new BigDecimal(0), balance);
		
		Map<String, Object> response1 = restTemplate.getForObject("http://localhost:" + port + "/v1/payment/login/mike",
				Map.class);

		assertNotNull(response1);

		// Asserting API Response
		Long id1 = (Long) response1.get("id");
		assertNotNull(id1);
		
		String name1 = (String) response1.get("name");
		assertNotNull(name1);
		assertEquals("mike", name1);
		
		BigDecimal balance1 = new BigDecimal ((String)response1.get("balance"));
		assertNotNull(balance1);
		assertEquals(new BigDecimal(0), balance1);
		
		Map<String, Object> response2 = restTemplate.getForObject("http://localhost:" + port + "/v1/payment/login/jack",
				Map.class);

		assertNotNull(response2);

		// Asserting API Response
		Long id2 = (Long) response2.get("id");
		assertNotNull(id2);
		assertEquals(id, id2);
		
		String name2 = (String) response2.get("name");
		assertNotNull(name2);
		assertEquals("jack", name2);
		
		BigDecimal balance2 = new BigDecimal ((String)response2.get("balance"));
		assertNotNull(balance2);
		assertEquals(new BigDecimal(0), balance2);
		
	
	
		String requestBody = "100";
		
        //===============================================
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<Map> responseE = restTemplate.exchange("http://localhost:" + port + "/v1/payment/"+ id +"/topup",
				HttpMethod.PATCH, entity, Map.class, Collections.EMPTY_MAP);

		assertNotNull(responseE);

		// Should return NO_CONTENT (status code 204)
		assertEquals(HttpStatus.NO_CONTENT, responseE.getStatusCode());
		
		Map<String, Object> response3 = restTemplate.getForObject("http://localhost:" + port + "/v1/payment/login/jack",
				Map.class);

		assertNotNull(response3);

		// Asserting API Response
		Long id3 = (Long) response3.get("id");
		assertNotNull(id3);
		assertEquals(id, id3);
		
		String name3 = (String) response3.get("name");
		assertNotNull(name3);
		assertEquals("jack", name3);
		
		BigDecimal balance3 = new BigDecimal((String)response3.get("balance"));
		assertNotNull(balance3);
		//assertEquals(new BigDecimal(100), balance3);
		assertEquals("100",(String)response3.get("balance"));
		
		
	    String requestBody1 = "80";
		
        //===============================================
		HttpHeaders headers1 = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity1 = new HttpEntity<>(requestBody1, headers);

		ResponseEntity<Map> responseE1 = restTemplate.exchange("http://localhost:" + port + "/v1/payment/"+ id1 +"/topup",
				HttpMethod.PATCH, entity1, Map.class, Collections.EMPTY_MAP);

		assertNotNull(responseE1);

		// Should return NO_CONTENT (status code 204)
		assertEquals(HttpStatus.NO_CONTENT, responseE1.getStatusCode());
		
		Map<String, Object> response4 = restTemplate.getForObject("http://localhost:" + port + "/v1/payment/login/mike",
				Map.class);

		assertNotNull(response4);

		// Asserting API Response
		Long id4 = (Long) response4.get("id");
		assertNotNull(id4);
		assertEquals(id1, id4);
		
		String name4 = (String) response4.get("name");
		assertNotNull(name4);
		assertEquals("mike", name4);
		
		BigDecimal balance4 = new BigDecimal((String)response4.get("balance"));
		assertNotNull(balance4);
		//assertEquals(new BigDecimal(100), balance3);
		assertEquals("80",(String)response4.get("balance"));
		
		//======================================
		 String requestBody2 = "50";
			
	        
			HttpHeaders headers2 = new HttpHeaders();
			headers.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity2 = new HttpEntity<>(requestBody2, headers);

			ResponseEntity<Map> responseE2 = restTemplate.exchange("http://localhost:" + port + "/v1/payment/"+ id1 +"/pay/"+id,
					HttpMethod.PATCH, entity2, Map.class, Collections.EMPTY_MAP);

			assertNotNull(responseE2);

			// Should return NO_CONTENT (status code 204)
			assertEquals(HttpStatus.NO_CONTENT, responseE2.getStatusCode());
			
			Map<String, Object> response5 = restTemplate.getForObject("http://localhost:" + port + "/v1/payment/login/mike",
					Map.class);

			assertNotNull(response5);
			// Asserting API Response
			Long id5 = (Long) response4.get("id");
			assertNotNull(id5);
			assertEquals(id1, id5);
			
			String name5 = (String) response4.get("name");
			assertNotNull(name5);
			assertEquals("mike", name5);
			
			BigDecimal balance5 = new BigDecimal((String)response5.get("balance"));
			assertNotNull(balance5);
			//assertEquals(new BigDecimal(100), balance3);
			assertEquals("30",(String)response5.get("balance"));
		
			Map<String, Object> response6 = restTemplate.getForObject("http://localhost:" + port + "/v1/payment/login/jack",
					Map.class);

			assertNotNull(response6);
			// Asserting API Response
			Long id6 = (Long) response6.get("id");
			assertNotNull(id6);
			assertEquals(id, id6);
			
			String name6 = (String) response6.get("name");
			assertNotNull(name6);
			assertEquals("jack", name6);
			
			BigDecimal balance6 = new BigDecimal((String)response5.get("balance"));
			assertNotNull(balance6);
			//assertEquals(new BigDecimal(100), balance3);
			assertEquals("150",(String)response6.get("balance"));
	}
}
