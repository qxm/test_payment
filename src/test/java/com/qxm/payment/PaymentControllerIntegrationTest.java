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
		//-----------user jack login-----------------
		Map<String, Object> cred = new HashMap<>();
		cred.put("username", "jack");
		cred.put("password", "user");
		HttpHeaders headers_1 = new HttpHeaders();
		headers_1.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity_1 = new HttpEntity<>(objectMapper.writeValueAsString(cred), headers_1);
		
		// API call
		ResponseEntity<Map>  response =  restTemplate.exchange("http://localhost:" + port + "/v1/payment/login",
						HttpMethod.POST, entity_1, Map.class, Collections.EMPTY_MAP);
			

		assertNotNull(response);

		// Asserting API Response
		String jwtToken = response.getHeaders().get("Authorization").get(0);
		assertEquals(114, jwtToken.length());
		//-----------------------user mike login--------------------------
		
		Map<String, Object> cred1 = new HashMap<>();
		cred1.put("username", "mike");
		cred1.put("password", "admin");
		HttpHeaders headers_2 = new HttpHeaders();
		headers_2.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity_2 = new HttpEntity<>(objectMapper.writeValueAsString(cred1), headers_2);
		
		// API call
		ResponseEntity<Map>  response1 =  restTemplate.exchange("http://localhost:" + port + "/v1/payment/login",
						HttpMethod.POST, entity_2, Map.class, Collections.EMPTY_MAP);
		

		assertNotNull(response1);

		// Asserting API Response
		String jwtToken1 = response1.getHeaders().get("Authorization").get(0);
		assertEquals(114, jwtToken1.length());
//--------------------------user jack login-----------------------
	
		Map<String, Object> cred2 = new HashMap<>();
		cred2.put("username", "jack");
		cred2.put("password", "user");
		HttpHeaders headers_3 = new HttpHeaders();
		headers_3.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity_3 = new HttpEntity<>(objectMapper.writeValueAsString(cred2), headers_3);
		
		// API call
		ResponseEntity<Map>  response2 =  restTemplate.exchange("http://localhost:" + port + "/v1/payment/login",
						HttpMethod.POST, entity_3, Map.class, Collections.EMPTY_MAP);
				

		assertNotNull(response);

		// Asserting API Response
		String jwtToken2 = response2.getHeaders().get("Authorization").get(0);
		assertEquals(114, jwtToken2.length());
		
		
		
        //=======================user jack top up 100========================
		String requestBody = "100.00";
		
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.add("Authorization", jwtToken2);
		HttpEntity<String> entity = new HttpEntity<>(requestBody, headers);

		ResponseEntity<Map> responseE = restTemplate.exchange("http://localhost:" + port + "/v1/payment/topup",
				HttpMethod.PATCH, entity, Map.class, Collections.EMPTY_MAP);

		assertNotNull(responseE);

		// Should return NO_CONTENT (status code 204)
		assertEquals(HttpStatus.NO_CONTENT, responseE.getStatusCode());
		
		//---------------------check jack balance-------------------------------------------
		HttpHeaders headers_11 = new HttpHeaders();
		//headers.setContentType(MediaType.APPLICATION_JSON);
		headers_11.add("Authorization", jwtToken2);
		HttpEntity<Void> entity_11 = new HttpEntity<>(headers_11);
		ResponseEntity<Map<String, Object>> response_11 = restTemplate.exchange(
				"http://localhost:" + port + "/v1/payment/client", HttpMethod.GET, entity_11, Map.class, Collections.EMPTY_MAP);
		
		Map<String, Object> response3 = response_11.getBody();
		assertNotNull(response3);

		// Asserting API Response
		Long id3 = new Long((Integer)response3.get("id"));
		assertNotNull(id3);
		
		
		String name3 = (String) response3.get("name");
		assertNotNull(name3);
		assertEquals("jack", name3);
		
		BigDecimal balance3 = new BigDecimal((String)response3.get("balance"));
		assertNotNull(balance3);
		//assertEquals(new BigDecimal(100), balance3);
		assertEquals("100.00",(String)response3.get("balance"));
		
		//-------------------user mike login-----------------------------
		Map<String, Object> cred_4 = new HashMap<>();
		cred_4.put("username", "mike");
		cred_4.put("password", "admin");
		HttpHeaders headers_4 = new HttpHeaders();
		headers_4.setContentType(MediaType.APPLICATION_JSON);
		HttpEntity<String> entity_4 = new HttpEntity<>(objectMapper.writeValueAsString(cred_4), headers_4);
		
		// API call
		ResponseEntity<Map>  response_4 =  restTemplate.exchange("http://localhost:" + port + "/v1/payment/login",
						HttpMethod.POST, entity_4, Map.class, Collections.EMPTY_MAP);
				

		assertNotNull(response_4);

		// Asserting API Response
		String jwtToken_4 = response_4.getHeaders().get("Authorization").get(0);
		assertEquals(114, jwtToken_4.length());
		
        //=====================mkie top up 80==========================
		 String requestBody1 = "80.00";
		HttpHeaders headers1 = new HttpHeaders();
		headers1.setContentType(MediaType.APPLICATION_JSON);
		headers1.add("Authorization", jwtToken_4);
		HttpEntity<String> entity1 = new HttpEntity<>(requestBody1, headers1);

		ResponseEntity<Map> responseE1 = restTemplate.exchange("http://localhost:" + port + "/v1/payment/topup",
				HttpMethod.PATCH, entity1, Map.class, Collections.EMPTY_MAP);

		assertNotNull(responseE1);

		// Should return NO_CONTENT (status code 204)
		assertEquals(HttpStatus.NO_CONTENT, responseE1.getStatusCode());
		
		//=================check mike balance========================================
		HttpHeaders headers_12 = new HttpHeaders();
		//headers.setContentType(MediaType.APPLICATION_JSON);
		headers_12.add("Authorization", jwtToken_4);
		HttpEntity<Void> entity_12 = new HttpEntity<>(headers_12);
		ResponseEntity<Map<String, Object>> response_12 = restTemplate.exchange(
				"http://localhost:" + port + "/v1/payment/client", HttpMethod.GET, entity_12, Map.class, Collections.EMPTY_MAP);
		
		Map<String, Object> response4 = response_12.getBody();

		assertNotNull(response4);

		// Asserting API Response
		Long id4 = new Long((Integer)response4.get("id"));
		assertNotNull(id4);
		
		
		String name4 = (String) response4.get("name");
		assertNotNull(name4);
		assertEquals("mike", name4);
		
		BigDecimal balance4 = new BigDecimal((String)response4.get("balance"));
		assertNotNull(balance4);
		//assertEquals(new BigDecimal(100), balance3);
		assertEquals("80.00",(String)response4.get("balance"));
		
		//====================mike pay jack 50==================
		 String requestBody2 = "50.00";
			
	        
			HttpHeaders headers2 = new HttpHeaders();
			headers2.setContentType(MediaType.APPLICATION_JSON);
			headers2.add("Authorization", jwtToken_4);
			HttpEntity<String> entity2 = new HttpEntity<>(requestBody2, headers2);

			ResponseEntity<Map> responseE2 = restTemplate.exchange("http://localhost:" + port + "/v1/payment/pay/"+"jack",
					HttpMethod.PATCH, entity2, Map.class, Collections.EMPTY_MAP);

			assertNotNull(responseE2);

			// Should return NO_CONTENT (status code 204)
			assertEquals(HttpStatus.NO_CONTENT, responseE2.getStatusCode());
			
		//===============check mike balance===========================================
			HttpHeaders headers_13 = new HttpHeaders();
			//headers.setContentType(MediaType.APPLICATION_JSON);
			headers_13.add("Authorization", jwtToken_4);
			HttpEntity<Void> entity_13 = new HttpEntity<>(headers_13);
			ResponseEntity<Map<String, Object>> response_13 = restTemplate.exchange(
					"http://localhost:" + port + "/v1/payment/client", HttpMethod.GET, entity_13, Map.class, Collections.EMPTY_MAP);
			
			Map<String, Object> response5 = response_13.getBody();

			assertNotNull(response5);
			// Asserting API Response
			Long id5 = new Long((Integer)response5.get("id"));
			assertNotNull(id5);
			assertEquals(id4, id5);
			
			String name5 = (String) response4.get("name");
			assertNotNull(name5);
			assertEquals("mike", name5);
			
			BigDecimal balance5 = new BigDecimal((String)response5.get("balance"));
			assertNotNull(balance5);
			//assertEquals(new BigDecimal(100), balance3);
			assertEquals("30.00",(String)response5.get("balance"));
		
			//--------------------------user jack login-----------------------
			
			Map<String, Object> cred3 = new HashMap<>();
			cred3.put("username", "jack");
			cred3.put("password", "user");
			HttpHeaders headers_5 = new HttpHeaders();
			headers_5.setContentType(MediaType.APPLICATION_JSON);
			HttpEntity<String> entity_5 = new HttpEntity<>(objectMapper.writeValueAsString(cred3), headers_5);
			
			// API call
			ResponseEntity<Map>  response_5 =  restTemplate.exchange("http://localhost:" + port + "/v1/payment/login",
							HttpMethod.POST, entity_5, Map.class, Collections.EMPTY_MAP);
			
			assertNotNull(response_5);

			// Asserting API Response
			String jwtToken_5 = response_5.getHeaders().get("Authorization").get(0);
			assertEquals(114, jwtToken_5.length());
			
			
			//=====================check jack balance=======================================
			HttpHeaders headers_14 = new HttpHeaders();
			//headers.setContentType(MediaType.APPLICATION_JSON);
			headers_14.add("Authorization", jwtToken_5);
			HttpEntity<Void> entity_14 = new HttpEntity<>(headers_14);
			ResponseEntity<Map<String, Object>> response_14 = restTemplate.exchange(
					"http://localhost:" + port + "/v1/payment/client", HttpMethod.GET, entity_14, Map.class, Collections.EMPTY_MAP);
			
			Map<String, Object> response6 = response_14.getBody();
		

			assertNotNull(response6);
			// Asserting API Response
			Long id6 = new Long((Integer)response6.get("id"));
			assertNotNull(id6);
			assertEquals(id3, id6);
			
			String name6 = (String) response6.get("name");
			assertNotNull(name6);
			assertEquals("jack", name6);
			
			BigDecimal balance6 = new BigDecimal((String)response5.get("balance"));
			assertNotNull(balance6);
			//assertEquals(new BigDecimal(100), balance3);
			assertEquals("150.00",(String)response6.get("balance"));
	}
}
