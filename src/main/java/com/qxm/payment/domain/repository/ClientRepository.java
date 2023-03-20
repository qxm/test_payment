package com.qxm.payment.domain.repository;


import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.qxm.payment.domain.model.entity.Client;

public interface ClientRepository  extends CrudRepository<Client, Long> {
	public Optional<Client> findByName(String name);
}
