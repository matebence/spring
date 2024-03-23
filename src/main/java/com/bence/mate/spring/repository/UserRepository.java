package com.bence.mate.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.bence.mate.spring.entity.UserEntity;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<UserEntity, Integer> {

    Optional<UserEntity> findByUsername(String username);
}
