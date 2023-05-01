package com.bence.mate.spring.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bence.mate.spring.entity.Person;

@Repository
public interface PersonRepository extends JpaRepository<Person, Long> {
}
