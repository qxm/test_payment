package com.qxm.payment.domain.service;
import java.util.List;
import java.util.Optional;

import com.qxm.payment.domain.model.entity.Client;

public interface PaymentService {
	
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Optional<Client> findClientByName(String name) throws Exception;
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public Client createClient(String name) throws Exception;
	
	public void topUp(Long id, String amount)  throws Exception;
	
	public void pay(Long fromId, Long toId, String amount)  throws Exception;
}
