package com.bence.mate.spring.service;

import org.springframework.data.domain.ExampleMatcher.StringMatcher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import com.bence.mate.spring.repository.ContactPagingRepository;
import com.bence.mate.spring.entity.Contact;

import java.util.Optional;

import static com.bence.mate.spring.repository.util.ContactExpressionUtil.hasMessage;
import static com.bence.mate.spring.repository.util.ContactExpressionUtil.hastStatus;

@Service
public class ContactService {

	@Autowired
	private ContactPagingRepository contactPagingRepository;

	public Page<Contact> findByStatusOrderBySubjectAndMesseageDesc() {
		return contactPagingRepository.findByStatus("DONE", PageRequest.of(0, 4, Sort.Direction.DESC, "subject", "message"));
	}

	public Iterable<Contact> findAllSortByStatusAsc() {
		Sort sortByLastName = Sort.by(Sort.Direction.ASC, "status");
		return contactPagingRepository.findAll(sortByLastName);
	}

	public Iterable<Contact> findByMessageAndStatus(String status, String message) {
		return contactPagingRepository.findAll(hastStatus(status).and(hasMessage(message)));
	}

	public Optional<Contact> findByMessage(String messeage) {
		return contactPagingRepository.findOne(Example.of(Contact.builder().message(messeage).build(), ExampleMatcher.matching().withIgnoreCase().withStringMatcher(StringMatcher.ENDING)));
	}
}
