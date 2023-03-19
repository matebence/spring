package com.bence.mate.spring.repository;

import org.springframework.stereotype.Repository;

import com.bence.mate.spring.repository.custom.ReadOnlyRepository;
import com.bence.mate.spring.entity.User;

@Repository
public interface UserRepository extends ReadOnlyRepository<User, Long> {
}
