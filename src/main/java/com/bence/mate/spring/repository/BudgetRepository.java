package com.bence.mate.spring.repository;

import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.bence.mate.spring.entity.Budget;

@Repository
public interface BudgetRepository extends PagingAndSortingRepository<Budget, Long> {
}
