package com.bence.mate.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.bence.mate.spring.entity.Credential;

@Repository
public interface CredentialRepository extends CrudRepository<Credential, Long> {
}
