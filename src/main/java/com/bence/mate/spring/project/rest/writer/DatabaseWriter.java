package com.bence.mate.spring.project.rest.writer;

import org.springframework.batch.item.database.ItemPreparedStatementSetter;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

import com.bence.mate.spring.project.rest.dto.Student;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import javax.sql.DataSource;

@Component
public class DatabaseWriter {

	@Autowired
	@Qualifier("studentdatasource")
	private DataSource dataSource;
	
	public JdbcBatchItemWriter<Student> write() {
		JdbcBatchItemWriter<Student> jdbcBatchItemWriter = new JdbcBatchItemWriter<>();
		
		jdbcBatchItemWriter.setSql("insert into student(id, firstName, lastName, email) values (?, ?, ?, ?)");
		jdbcBatchItemWriter.setItemPreparedStatementSetter(new StudentPreparedStatementSetter());
		jdbcBatchItemWriter.setDataSource(dataSource);
		
		return jdbcBatchItemWriter;
	}
	
	private static class StudentPreparedStatementSetter implements ItemPreparedStatementSetter<Student> {

		@Override
		public void setValues(Student item, PreparedStatement ps) throws SQLException {
			ps.setLong(1, item.getId());
			ps.setString(2, item.getFirstName());
			ps.setString(3, item.getLastName());
			ps.setString(4, item.getEmail());			
		}
	}
}
