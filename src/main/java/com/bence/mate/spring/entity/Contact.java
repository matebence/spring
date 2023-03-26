package com.bence.mate.spring.entity;

import jakarta.persistence.SqlResultSetMappings;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.NamedNativeQueries;
import jakarta.persistence.NamedNativeQuery;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.ColumnResult;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;

import lombok.experimental.SuperBuilder;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "contact_msg")
@SqlResultSetMappings({
		@SqlResultSetMapping(name = "SqlResultSetMapping.count", columns = @ColumnResult(name = "cnt")) 
})
@NamedQueries({ @NamedQuery(name = "Contact.findOpenMsgs", query = "SELECT c FROM Contact c WHERE c.status = :status"),
		@NamedQuery(name = "Contact.updateMsgStatus", query = "UPDATE Contact c SET c.status = ?1 WHERE c.contactId = ?2") 
})
@NamedNativeQueries({
		@NamedNativeQuery(name = "Contact.findOpenMsgsNative", query = "SELECT * FROM contact_msg c WHERE c.status = :status", resultClass = Contact.class),
		@NamedNativeQuery(name = "Contact.findOpenMsgsNative.count", query = "select count(*) as cnt from contact_msg c where c.status = :status", resultSetMapping = "SqlResultSetMapping.count"),
		@NamedNativeQuery(name = "Contact.updateMsgStatusNative", query = "UPDATE contact_msg c SET c.status = ?1 WHERE c.contact_id = ?2") 
})
public class Contact extends BaseEntity {

	@Id
	@Getter
	@Setter
	@Column(name = "contact_id")
	@GeneratedValue(strategy = GenerationType.AUTO)
	private int contactId;

	@Getter
	@Setter
	@Column(name = "name")
	private String name;

	@Getter
	@Setter
	@Column(name = "mobile_num")
	private String mobileNum;

	@Getter
	@Setter
	@Column(name = "email")
	private String email;

	@Getter
	@Setter
	@Column(name = "subject")
	private String subject;

	@Getter
	@Setter
	@Column(name = "message")
	private String message;

	@Getter
	@Setter
	@Column(name = "status")
	private String status;
}
