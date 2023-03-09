package com.qxm.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentControllerIntegrationTests {

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
	public void testLogin() {
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
		
		BigDecimal balance = new BigDecimal ((Integer)response.get("balance"));
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
		
		BigDecimal balance1 = new BigDecimal ((Integer)response1.get("balance"));
		assertNotNull(balance1);
		assertEquals(new BigDecimal(0), balance1);
		
		Map<String, Object> response2 = restTemplate.getForObject("http://localhost:" + port + "/v1/payment/login/alice",
				Map.class);

		assertNotNull(response2);

		// Asserting API Response
		Long id2 = (Long) response.get("id");
		assertNotNull(id2);
		assertEquals(id, id2);
		
		String name2 = (String) response.get("name");
		assertNotNull(name2);
		assertEquals("jack", name2);
		
		BigDecimal balance2 = new BigDecimal ((Integer)response.get("balance"));
		assertNotNull(balance2);
		assertEquals(new BigDecimal(0), balance2);
		
	
	}
}
