package com.bence.mate.spring.repository.util;

import com.querydsl.core.types.dsl.BooleanExpression;
import com.bence.mate.spring.entity.QContact;

public class ContactExpressionUtil {

	public static BooleanExpression hastStatus(String status) {
		return QContact.contact.status.eq(status);
	}

	public static BooleanExpression hasMessage(String message) {
		return QContact.contact.message.eq(message);
	}
}
