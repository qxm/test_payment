package com.qxm.payment.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.model.entity.Payment;
import com.qxm.payment.domain.repository.ClientRepository;

/**
 * 
 * @author qxm
 *
 */
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {
	private ClientRepository clientRepository;
	
	/**
	 * 
	 * @param clientRepository
	 */
	@Autowired
	public
	PaymentServiceImpl(ClientRepository repository) {
		this.clientRepository = repository;
	}


	@Override
	public Client findClientByName(String name) throws Exception {
		return clientRepository.findByName(name);
	}
	
	@Override
	public Client createClient(String name) throws Exception {
		Client  client = new Client(name, new BigDecimal(0));
		clientRepository.save(client);
		return client;
	}
	
	public void topUp(Long id, String amount)  throws Exception {
		topUp(id, new BigDecimal(amount));
	}
	
	public void pay(Long fromId, Long toId, String amount)  throws Exception {
		Client from = clientRepository.findById(fromId).get();
		Client to = clientRepository.findById(toId).get();
		Payment payment = new Payment(from, to, new BigDecimal(amount));
		pay(payment);
	}


	
	private void topUp(Long id, BigDecimal amount) {
		Client client =clientRepository.findById(id).get();
	    if (client != null) {
	    	client.setBalance(client.getBalance().add(amount));
	    	if (amount.compareTo(new BigDecimal(0))>0 && client.getPayments().size()>0) {
	    		processPendingPayment(client);
	    	}
	    }
	}
	
	private void processPendingPayment(Client client) {
		List<Payment> results = new ArrayList<>();
		List<Payment> processList = client.getPayments();
		client.setPayments(new ArrayList<>());
		for (Payment payment: processList) {
		
			Client from = payment.getFrom();
			Client to = payment.getTo();
			BigDecimal amount = payment.getAmount();
			BigDecimal balance = from.getBalance();
			BigDecimal received = new BigDecimal(0);
	    	if (balance.compareTo(amount) >=0) {
	    	  from.setBalance(from.getBalance().subtract(amount));
	    	  to.setBalance(to.getBalance().add(amount));
	    	  received = amount;
	    	} else {
	    		from.setBalance(new BigDecimal(0));
		    	to.setBalance(to.getBalance().add(balance));
		    	Payment remain = new Payment(payment.getFrom(), payment.getTo(), payment.getAmount().subtract(balance));
		    	
		    	results.add(remain);
		    	received = balance;
	    	}
	    	if (received.compareTo(new BigDecimal(0))>0 && to.getPayments().size()>0) {
	    		processPendingPayment(to);
	    	}
		}
		client.setPayments(results);
		
		
	}


	private void pay(Payment payment) {
		Client from = payment.getFrom();
		Client to = payment.getTo();
		
	
		
	    if (from != null && to != null) {
	    	from.addPayment(payment);
	    	processPendingPayment(from);
	    }
		
	}
}
