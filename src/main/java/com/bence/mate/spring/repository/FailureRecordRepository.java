package com.bence.mate.spring.repository;

import org.springframework.data.repository.CrudRepository;
import com.bence.mate.spring.entity.FailureRecord;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FailureRecordRepository extends CrudRepository<FailureRecord,Integer> {
    List<FailureRecord> findAllByStatus(String status);
}
