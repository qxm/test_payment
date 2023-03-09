package com.qxm.payment.domain.service;

import java.util.Collection;

import com.qxm.payment.domain.repository.Repository;

/**
 * 
 * @author qxm
 *
 * @param <TE>
 * @param <T>
 */
public abstract class BaseService<TE,T> {
	private Repository<TE, T> _repository;

    BaseService(Repository<TE, T> repository) {
        _repository = repository;
    }

    /**
     *
     * @param entity
     * @throws Exception
     */
    public void add(TE entity) throws Exception {
        _repository.add(entity);
    }

    /**
     *
     * @return
     */
    public Collection<TE> getAll() {
        return _repository.getAll();
    }
}

