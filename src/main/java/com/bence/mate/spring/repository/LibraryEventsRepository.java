package com.bence.mate.spring.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.bence.mate.spring.entity.LibraryEvent;

@Repository
public interface LibraryEventsRepository extends CrudRepository<LibraryEvent,Integer> {
}
