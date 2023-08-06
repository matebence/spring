package com.bence.mate.spring.project.rest.reader;

import org.springframework.batch.item.database.JdbcCursorItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.stereotype.Component;

import com.bence.mate.spring.project.rest.dto.Student;
import javax.sql.DataSource;

@Component
public class DatabseReader {
	
	@Autowired
	@Qualifier("studentdatasource")
	private DataSource dataSource;
	
	public JdbcCursorItemReader<Student> read() {
		BeanPropertyRowMapper<Student> studentRowMapper = new BeanPropertyRowMapper<>();
		studentRowMapper.setMappedClass(Student.class);
		
		JdbcCursorItemReader<Student> jdbcCursorItemReader = new JdbcCursorItemReader<>();
		
		jdbcCursorItemReader.setDataSource(dataSource);
		jdbcCursorItemReader.setRowMapper(studentRowMapper);
		jdbcCursorItemReader.setSql("select id, firstName, lastName, email from student");
		
		jdbcCursorItemReader.setCurrentItemCount(1);
		jdbcCursorItemReader.setMaxItemCount(8);
		
		return jdbcCursorItemReader;
	}
}
