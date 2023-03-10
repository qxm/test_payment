package com.qxm.payment.domain.service;

import java.math.BigDecimal;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.qxm.payment.domain.model.entity.BaseEntity;
import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.model.entity.Payment;
import com.qxm.payment.domain.repository.ClientRepository;

/**
 * 
 * @author qxm
 *
 */
@Service("paymentService")
public class PaymentServiceImpl extends BaseService<Client, Long> implements PaymentService {
	private ClientRepository clientRepository;
	
	/**
	 * 
	 * @param clientRepository
	 */
	@Autowired
	public
	PaymentServiceImpl(ClientRepository repository) {
		super(repository);
		this.clientRepository = repository;
	}

	@Override
	public void add(Client client) throws Exception {
		clientRepository.add(client);
		
	}

	@Override
	public void update(Client client) throws Exception {
		clientRepository.update(client);
		
	}

	@Override
	public void delete(Long id) throws Exception {
		clientRepository.remove(id);
		
	}

	@Override
	public BaseEntity<?> findById(Long id) throws Exception {
		return clientRepository.get(id);
	}


	@Override
	public BaseEntity<?> findClientByName(String name) throws Exception {
		return clientRepository.findClientByName(name);
	}
	
	@Override
	public BaseEntity<?> createClient(String name) throws Exception {
		Client  client = new Client(System.currentTimeMillis()+1001L, name, new BigDecimal(0));
		clientRepository.add(client);
		return client;
	}
	
	public void topUp(Long id, String amount)  throws Exception {
		clientRepository.topUp(id, new BigDecimal(amount));
	}
	
	public void pay(Long from, Long to, String amount)  throws Exception {
		Payment payment = new Payment(System.currentTimeMillis(), from, to, new BigDecimal(amount));
		clientRepository.pay(payment);
	}
}
