package com.qxm.payment.domain.model.entity;

import java.math.BigDecimal;

public class Payment extends BaseEntity<Long> {
	private Long from;
	private Long to;
	private BigDecimal amount;

	public Payment(Long id, Long from, Long to, BigDecimal amount) {
		super(id);
		setFrom(from);
		setTo(to);
		setAmount(amount);
	}

	public Long getFrom() {
		return from;
	}

	public void setFrom(Long from) {
		this.from = from;
	}

	public Long getTo() {
		return to;
	}

	public void setTo(Long to) {
		this.to = to;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}
	
}
