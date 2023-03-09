package com.qxm.payment.domain.model.entity;

/**
 * 
 * @author qxm
 *
 * @param <T>
 */
public class BaseEntity<T> {
	T id;
 
	public BaseEntity (T id) {
		this.id = id;
	}
	
	/**
	 * 
	 * @return
	 */
	public T getId() {
		return id;
	}

	/**
	 *
	 * @param id
	 */
	public void setId(T id) {
		this.id = id;
	}
}
