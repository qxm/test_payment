package com.qxm.payment.domain.repository;

import java.util.Collection;

/**
 * 
 * @author qxm
 *
 * @param <TE>
 * @param <T>
 */
public interface Repository<TE,T> {

	    /**
	     *
	     * @param id
	     * @return
	     */
	    TE get(T id);

	    /**
	     *
	     * @return
	     */
	    Collection<TE> getAll();

	    /**
	     *
	     * @param entity
	     */
	    void add(TE entity);

	    /**
	     *
	     * @param id
	     */
	    void remove(T id);

	    /**
	     *
	     * @param entity
	     */
	    void update(TE entity);

}
