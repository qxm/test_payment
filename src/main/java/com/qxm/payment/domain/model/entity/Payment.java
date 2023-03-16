package com.qxm.payment.domain.model.entity;

import java.math.BigDecimal;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Payment {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private long id;

	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="from")
	private Client from;
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="to")
	private Client to;
	private BigDecimal amount;

	public Payment(Client from, Client to, BigDecimal amount) {
		super();
		setFrom(from);
		setTo(to);
		setAmount(amount);
	}
	
	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}


	public Client getFrom() {
		return from;
	}

	public void setFrom(Client from) {
		this.from = from;
	}

	public Client getTo() {
		return to;
	}

	public void setTo(Client to) {
		this.to = to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
