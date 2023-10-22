package com.bence.mate.author.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.bence.mate.author.entity.Author;
@Repository
public interface AuthorRepository extends JpaRepository<Author, Long> {
}
