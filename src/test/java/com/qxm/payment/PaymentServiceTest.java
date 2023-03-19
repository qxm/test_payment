package com.qxm.payment;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.math.BigDecimal;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.repository.ClientRepository;
import com.qxm.payment.domain.service.PaymentService;
import com.qxm.payment.domain.service.PaymentServiceImpl;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PaymentServiceTest {
	
	@Autowired
	private PaymentService paymentService;
	
    
	@Test
    public void testCreateClient() throws Exception
    {
    	
    	List<Client> list = paymentService.findClientByName("Alice");
    	assertEquals(0, list.size());
    	Client alice = (Client) paymentService.createClient("Alice");
    	assertNotNull(alice);
    	assertEquals("Alice", alice.getName());
    	assertEquals(new BigDecimal("0.00"), alice.getBalance());
    	
    	list = paymentService.findClientByName("Bob");
    	assertEquals(0, list.size());
    	Client bob = (Client) paymentService.createClient("Bob");
    	assertNotNull(bob);
    	assertEquals("Bob", bob.getName());
    	assertEquals(new BigDecimal("0.00"), bob.getBalance());
    	
    	paymentService.topUp(alice.getId(), "100.00");
    	Client alice1 = paymentService.findClientByName("Alice").get(0);
    	assertEquals(new BigDecimal("100.00"), alice1.getBalance());
    	assertEquals(alice.getId(), alice1.getId());
    	
    	paymentService.topUp(bob.getId(), "80.00");
    	bob = paymentService.findClientByName("Bob").get(0);
    	assertEquals(new BigDecimal("80.00"), bob.getBalance());
    	
    	paymentService.pay(bob.getId(), alice.getId(), "50.00");
    	 alice = paymentService.findClientByName("Alice").get(0);
    	 bob = paymentService.findClientByName("Bob").get(0);
    	assertEquals(new BigDecimal("30.00"), bob.getBalance());
    	assertEquals(new BigDecimal("150.00"), alice.getBalance());
    	
    	paymentService.pay(bob.getId(), alice.getId(), "100.00");
    	alice = paymentService.findClientByName("Alice").get(0);
    	bob = paymentService.findClientByName("Bob").get(0);
    	//System.out.println("------------------bob balance before:"+bob.getBalance()+"paymentsize:"+ bob.getPayments().size());
    	assertEquals(new BigDecimal("0.00"), bob.getBalance());
    	assertEquals(new BigDecimal("180.00"), alice.getBalance());
    	
    	
    	paymentService.topUp(bob.getId(), "30.00");
    	alice = paymentService.findClientByName("Alice").get(0);
    	bob = paymentService.findClientByName("Bob").get(0);
    	//System.out.println("------------------bob balance before:"+bob.getBalance()+"paymentsize:"+ bob.getPayments().size());
       
    	assertEquals(new BigDecimal("0.00"), bob.getBalance());
    	assertEquals(new BigDecimal("210.00"), alice.getBalance());
    	
    	
    	paymentService.pay(alice.getId(), bob.getId(), "30.00");
    	alice = paymentService.findClientByName("Alice").get(0);
    	bob = paymentService.findClientByName("Bob").get(0);
    	assertEquals(new BigDecimal("0.00"), bob.getBalance());
    	assertEquals(new BigDecimal("210.00"), alice.getBalance());
    	
    	//System.out.println("------------------bob balance before:"+bob.getBalance()+"paymentsize:"+ bob.getPayments().size());
    	paymentService.topUp(bob.getId(), "100.00");
    	//System.out.println("------------------bob balance after:"+bob.getBalance());
    	alice = paymentService.findClientByName("Alice").get(0);
    	bob = paymentService.findClientByName("Bob").get(0);
    	assertEquals(new BigDecimal("90.00"), bob.getBalance());
    	assertEquals(new BigDecimal("220.00"), alice.getBalance());
    }
}
