package com.qxm.payment.domain.repository;

import org.springframework.data.repository.CrudRepository;

import com.qxm.payment.domain.model.entity.Client;
import com.qxm.payment.domain.model.entity.Payment;

public interface PaymentRepository  extends CrudRepository<Payment, Long> {

}
