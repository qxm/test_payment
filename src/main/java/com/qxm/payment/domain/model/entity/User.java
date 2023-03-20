package com.qxm.payment.domain.model.entity;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;

@Entity
public class User {
	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	@Column(nullable=false, updatable = false)
	private Long id;
	
	@Column(nullable=false, unique=true)
	private String username;
	
	@Column(nullable=false)
	private String password;
	
	@OneToOne(mappedBy="user", fetch = FetchType.LAZY, cascade=CascadeType.ALL, orphanRemoval = true)
	private Client client;
	
	public User() {
		
	}
	
	public User(String username, String password, Client client) {
		super();
		this.username = username;
		this.password = password;
		this.client = client;
	}
	
	public User(String username, String password) {
		this(username, password, new Client(username));
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}
}
