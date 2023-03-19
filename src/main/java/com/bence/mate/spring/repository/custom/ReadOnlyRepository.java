package com.bence.mate.spring.repository.custom;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.Repository;

import java.util.Optional;

@NoRepositoryBean
//Any another custom method has to have its implementation class
public interface ReadOnlyRepository<T, ID> extends Repository<T, ID> {

	Optional<T> findById(ID id);

	boolean existsById(ID id);

	Iterable<T> findAll();

	Iterable<T> findAllById(Iterable<ID> ids);

	long count();
}
