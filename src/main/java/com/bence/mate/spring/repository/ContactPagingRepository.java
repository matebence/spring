package com.bence.mate.spring.repository;

import org.springframework.data.repository.query.QueryByExampleExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.bence.mate.spring.entity.Contact;

import com.querydsl.core.types.Predicate;

import jakarta.transaction.Transactional;

@Repository
public interface ContactPagingRepository extends PagingAndSortingRepository<Contact, Long>, QuerydslPredicateExecutor<Contact>, QueryByExampleExecutor<Contact> {

	@Transactional
	@Modifying
	int updateMsgStatus(String status, int id);

	@Transactional
	@Modifying
	@Query("UPDATE Contact c SET c.status = ?1 WHERE c.contactId = ?2")
	int updateStatusById(String status, int id);

	@Transactional
	@Modifying
	@Query(nativeQuery = true)
	int updateMsgStatusNative(String status, int id);

	@Query(nativeQuery = true)
	Page<Contact> findOpenMsgsNative(@Param("status") String status, Pageable pageable);

	@Query("SELECT c FROM Contact c WHERE c.status = :status")
	Page<Contact> findByStatus(@Param("status") String status, Pageable pageable);

	@Query(value = "SELECT * FROM contact_msg c WHERE c.status = :status", nativeQuery = true)
	Page<Contact> findByStatusNative(@Param("status") String status, Pageable pageable);

	Iterable<Contact> findAll(Predicate predicate);

	Iterable<Contact> findAll(Predicate predicate, Sort sort);

	Page<Contact> findAll(Predicate predicate, Pageable pageable);

	long count(Predicate predicate);
}
