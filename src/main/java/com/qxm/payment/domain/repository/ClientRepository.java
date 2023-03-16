package com.qxm.payment.domain.repository;


import org.springframework.data.repository.CrudRepository;

import com.qxm.payment.domain.model.entity.Client;

public interface ClientRepository  extends CrudRepository<Client, Long> {
	public Client findByName(String name);
}
