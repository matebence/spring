package com.bence.mate.spring.repository.mapper;

import org.springframework.jdbc.core.RowMapper;

import com.bence.mate.spring.entity.Contact;

import java.sql.SQLException;
import java.sql.ResultSet;

public class ContactRowMapper implements RowMapper<Contact> {

	@Override
	public Contact mapRow(ResultSet rs, int rowNum) throws SQLException {
		Contact contact = new Contact();
		contact.setContactId(rs.getInt("contact_id"));
		contact.setName(rs.getString("name"));
		contact.setMobileNum(rs.getString("mobile_num"));
		contact.setEmail(rs.getString("email"));
		contact.setSubject(rs.getString("subject"));
		contact.setMessage(rs.getString("message"));
		contact.setStatus(rs.getString("status"));
		contact.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
		contact.setCreatedBy(rs.getString("created_by"));

		if (null != rs.getTimestamp("update_at")) {
			contact.setUpdatedAt(rs.getTimestamp("updated_at").toLocalDateTime());
		}
		contact.setUpdatedBy(rs.getString("updated_by"));
		return contact;
	}
}
