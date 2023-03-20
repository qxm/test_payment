package com.qxm.payment.domain.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import com.qxm.payment.domain.model.entity.User;

public interface UserRepository extends CrudRepository<User, Long> {
	Optional<User> findByUsername(String username);

}
