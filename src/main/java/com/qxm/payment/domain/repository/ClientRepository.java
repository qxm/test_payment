package com.qxm.payment.domain.repository;


import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.qxm.payment.domain.model.entity.Client;

public interface ClientRepository  extends CrudRepository<Client, Long> {
	public List<Client> findByName(String name);
}
