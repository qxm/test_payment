package com.qxm.payment.domain.model.entity;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

@Entity
public class Client {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;
	@Column(nullable=false, unique=true)
	private String name;

	private BigDecimal balance;
	@OneToOne
	@JoinColumn(name="USER_ID")
	private User user;
	@OneToMany(fetch = FetchType.EAGER, cascade=CascadeType.ALL, mappedBy="payFrom")
	private Set<Payment> payments;
	
	public Client() {
		
	}
	public Client(String name) {
		this(name, new BigDecimal("0.00"));
	}
	public Client(String name, BigDecimal balance) {
	    super();
		this.setName(name);
		this.setBalance(balance);
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
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

	public Set<Payment> getPayments() {
		return payments;
	}

	public void setPayments(Set<Payment> payments) {
		this.payments = payments;
	}
	
	public void addPayment(Payment payment) {
	
		payments.add(payment);
	}
	
}