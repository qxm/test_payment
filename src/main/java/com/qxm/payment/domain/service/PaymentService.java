package com.qxm.payment.domain.service;

import com.qxm.payment.domain.model.entity.BaseEntity;
import com.qxm.payment.domain.model.entity.Client;

public interface PaymentService {
	/**
	 * 
	 * @param client
	 * @throws Exception
	 */
	public void add(Client client) throws Exception;

	/**
	 * 
	 * @param client
	 * @throws Exception
	 */
	public void update(Client client) throws Exception;

	/**
	 * 
	 * @param id
	 * @throws Exception
	 */
	public void delete(Long id) throws Exception;

	/**
	 * 
	 * @param id
	 * @return
	 * @throws Exception
	 */
	public BaseEntity<?> findById(Long id) throws Exception;
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public BaseEntity<?> findClientByName(String name) throws Exception;
	
	/**
	 * 
	 * @param name
	 * @return
	 * @throws Exception
	 */
	public BaseEntity<?> createClient(String name) throws Exception;
	
	public void topUp(Long id, String amount)  throws Exception;
	
	public void pay(Long from, Long to, String amount)  throws Exception;
}
