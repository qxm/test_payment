package com.qxm.payment.domain.repository;

import java.math.BigDecimal;

import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.model.entity.Payment;

public interface ClientRepository  extends Repository<Client, Long> {
	public Client findClientByName(String name);
	public void topUp(Long id, BigDecimal amount);
    public void pay(Payment payment);
}
