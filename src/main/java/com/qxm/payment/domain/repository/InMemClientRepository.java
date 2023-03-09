package com.qxm.payment.domain.repository;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Repository;

import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.model.entity.Payment;

@Repository("clientrepository")
public class InMemClientRepository implements ClientRepository {
	private Map<Long, Client> entities = new HashMap<>();;
	@Override
	public Client get(Long id) {
		return entities.get(id);
	}

	@Override
	public Collection<Client> getAll() {
		return entities.values();
	}

	@Override
	public void add(Client entity) {
		entities.put(entity.getId(), entity);
		
	}

	@Override
	public void remove(Long id) {
		if (entities.containsKey(id)) {
			entities.remove(id);
		}
		
	}

	@Override
	public void update(Client entity) {
		if (entities.containsKey(entity.getId())) {
			entities.put(entity.getId(), entity);
		}
		
	}


	@Override
	public Client findClientByName(String name) {
		for (Client client: entities.values()) {
			if (client.getName() == name) return client;
		}
		return null;
	}

	@Override
	public void topUp(Long id, BigDecimal amount) {
		Client client = entities.get(id);
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
			Long fromId = payment.getFrom();
			Long toId= payment.getTo();
			Client from = entities.get(fromId);
			Client to = entities.get(toId);
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
		    	Payment remain = new Payment(payment.getId(), payment.getFrom(), payment.getTo(), payment.getAmount().subtract(balance));
		    	
		    	results.add(remain);
		    	received = balance;
	    	}
	    	if (received.compareTo(new BigDecimal(0))>0 && to.getPayments().size()>0) {
	    		processPendingPayment(to);
	    	}
		}
		client.setPayments(results);
		
		
	}

	@Override
	public void pay(Payment payment) {
		
		Long fromId = payment.getFrom();
		Long toId= payment.getTo();
		Client from = entities.get(fromId);
		Client to = entities.get(toId);
		
	    if (from != null && to != null) {
	    	from.addPayment(payment);
	    	processPendingPayment(from);
	    }
		
	}

}
