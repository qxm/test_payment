package com.qxm.payment.domain.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.model.entity.Payment;
import com.qxm.payment.domain.repository.ClientRepository;
import com.qxm.payment.domain.repository.PaymentRepository;

/**
 * 
 * @author qxm
 *
 */
@Service("paymentService")
public class PaymentServiceImpl implements PaymentService {
	private ClientRepository clientRepository;
	@Autowired
	private PaymentRepository paymentRepository;
	
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
	public Optional<Client> findClientByName(String name) throws Exception {
		return clientRepository.findByName(name);
	}
	
	@Override
	public Client createClient(String name) throws Exception {
		Client  client = new Client(name, new BigDecimal("0.00"));
		clientRepository.save(client);
		return client;
	}
	
	public void topUp(Long id, String amount)  throws Exception {
		topUp(id, new BigDecimal(amount));
	}
	
	@Transactional
	public void pay(Long fromId, Long toId, String amount)  throws Exception {
		Client from = clientRepository.findById(fromId).get();
		Client to = clientRepository.findById(toId).get();
		Payment found = null;
		for (Payment payment: to.getPayments()) {
			if (payment.getTo().getId() == from.getId()) {
				found = payment;
				break;
			}
		}
		BigDecimal payAmount = new BigDecimal(amount);
		if (found == null) {
			preparePay(new Payment(from, to, payAmount));
		
		    clientRepository.save(from);
		} else {
			BigDecimal tranferAmount = payAmount.subtract(found.getAmount());
			if (tranferAmount.compareTo(new BigDecimal("0.00"))<0) {
				found.setAmount(found.getAmount().subtract(payAmount));
				paymentRepository.save(found);
				clientRepository.save(to);
			} else {
				to.getPayments().remove(found);
				paymentRepository.delete(found);
				Payment payment = new Payment(from, to, tranferAmount);
				pay(payment);
				clientRepository.save(from);
				clientRepository.save(to);
			}
		}
	}


	@Transactional
	private void topUp(Long id, BigDecimal amount) {
		Client client =clientRepository.findById(id).get();
	    if (client != null) {
	    	client.setBalance(client.getBalance().add(amount));
	    	if (amount.compareTo(new BigDecimal("0.00"))>0 && client.getPayments().size()>0) {
	    		processPendingPayment(client);
	    	}
	    	
	    	clientRepository.save(client);
	    }
	}
	
	private void processPendingPayment(Client client) {

		Set<Payment> processList = client.getPayments();
		client.setPayments(new HashSet<>());
	   	for (Payment payment: processList) {
			
			pay(payment);
			
		}
		
		
	}


	private void pay(Payment payment) {
		Client from = payment.getPayFrom();
		Client to = payment.getTo();
		BigDecimal amount = payment.getAmount();
		BigDecimal balance = from.getBalance();
    	if (balance.compareTo(amount) >=0) {
	    	  from.setBalance(from.getBalance().subtract(amount));
	    	  to.setBalance(to.getBalance().add(amount));
	    	  
	    	} else {
	    		from.setBalance(new BigDecimal("0.00"));
		    	to.setBalance(to.getBalance().add(balance));
		    	payment.setAmount(payment.getAmount().subtract(balance));
		    	
		    	from.addPayment(payment);
		 
	    	}
    	clientRepository.save(to);
	}
	
	private void preparePay(Payment payment1) {
		Client from = payment1.getPayFrom();
		Client to = payment1.getTo();
		Payment found1 = null;
		Payment payment = new Payment(from, to, payment1.getAmount());
		for (Payment item: from.getPayments()) {
			if (item.getTo().getId() == to.getId()) {
				found1 = item;
				break;
			}
		}
		if (found1 != null) {
	        from.getPayments().remove(found1);
			paymentRepository.delete(found1);
			payment = new Payment(from, to, payment1.getAmount().add(found1.getAmount()));
		}
	    
	    	
		  pay(payment);
	    	
	    	
	    	
	}
}
