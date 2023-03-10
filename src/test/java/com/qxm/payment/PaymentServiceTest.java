package com.qxm.payment;

import java.math.BigDecimal;

import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.repository.ClientRepository;
import com.qxm.payment.domain.repository.InMemClientRepository;
import com.qxm.payment.domain.service.PaymentService;
import com.qxm.payment.domain.service.PaymentServiceImpl;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

public class PaymentServiceTest extends TestCase {
	private ClientRepository clientRepository = new InMemClientRepository();
	private PaymentService paymentService =  new PaymentServiceImpl(clientRepository);
	
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public PaymentServiceTest( String testName )
    {
        super(testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( PaymentServiceTest.class );
    }
    
    public void testCreateClient() throws Exception
    {
    	
    	Client alice = (Client) paymentService.findClientByName("Alice");
    	assertEquals(null, alice);
    	alice = (Client) paymentService.createClient("Alice");
    	assertNotNull(alice);
    	assertEquals("Alice", alice.getName());
    	assertEquals(new BigDecimal(0), alice.getBalance());
    	
    	Client bob = (Client) paymentService.findClientByName("Bob");
    	assertEquals(null, bob);
    	bob = (Client) paymentService.createClient("Bob");
    	assertNotNull(bob);
    	assertEquals("Bob", bob.getName());
    	assertEquals(new BigDecimal(0), bob.getBalance());
    	
    	paymentService.topUp(alice.getId(), "100");
    	Client alice1 = (Client) paymentService.findClientByName("Alice");
    	assertEquals(new BigDecimal(100), alice1.getBalance());
    	assertEquals(alice.getId(), alice1.getId());
    	
    	paymentService.topUp(bob.getId(), "80");
    	bob = (Client) paymentService.findClientByName("Bob");
    	assertEquals(new BigDecimal(80), bob.getBalance());
    	
    	paymentService.pay(bob.getId(), alice.getId(), "50");
    	 alice = (Client) paymentService.findClientByName("Alice");
    	 bob = (Client) paymentService.findClientByName("Bob");
    	assertEquals(new BigDecimal(30), bob.getBalance());
    	assertEquals(new BigDecimal(150), alice.getBalance());
    	
    	paymentService.pay(bob.getId(), alice.getId(), "100");
    	alice = (Client) paymentService.findClientByName("Alice");
    	bob = (Client) paymentService.findClientByName("Bob");
    	assertEquals(new BigDecimal(0), bob.getBalance());
    	assertEquals(new BigDecimal(180), alice.getBalance());
    	
    	
    	paymentService.topUp(bob.getId(), "30");
    	alice = (Client) paymentService.findClientByName("Alice");
    	bob = (Client) paymentService.findClientByName("Bob");
    	assertEquals(new BigDecimal(0), bob.getBalance());
    	assertEquals(new BigDecimal(210), alice.getBalance());
    	
    	
    	paymentService.pay(alice.getId(), bob.getId(), "30");
    	alice = (Client) paymentService.findClientByName("Alice");
    	bob = (Client) paymentService.findClientByName("Bob");
    	assertEquals(new BigDecimal(0), bob.getBalance());
    	assertEquals(new BigDecimal(210), alice.getBalance());
    	
    	paymentService.topUp(bob.getId(), "100");
    	alice = (Client) paymentService.findClientByName("Alice");
    	bob = (Client) paymentService.findClientByName("Bob");
    	assertEquals(new BigDecimal(90), bob.getBalance());
    	assertEquals(new BigDecimal(220), alice.getBalance());
    }
}
