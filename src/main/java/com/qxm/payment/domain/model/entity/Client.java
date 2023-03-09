package com.qxm.payment.domain.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class Client extends BaseEntity<Long> {
	private String name;

	private BigDecimal balance;
	private List<Payment> payments = new ArrayList<>();
	public Client(Long id, String name, BigDecimal balance) {
		super(id);
		this.setName(name);
		this.setBalance(balance);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public BigDecimal getBalance() {
		return balance;
	}

	public void setBalance(BigDecimal balance) {
		this.balance = balance;
	}

	public List<Payment> getPayments() {
		return payments;
	}

	public void setPayments(List<Payment> payments) {
		this.payments = payments;
	}
	
	public void addPayment(Payment payment) {
		payments.add(payment);
	}
	
}