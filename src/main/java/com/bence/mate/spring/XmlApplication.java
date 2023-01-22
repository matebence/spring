package com.bence.mate.spring;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import com.bence.mate.spring.xml.Elastic;
import com.bence.mate.spring.xml.Mongo;
import com.bence.mate.spring.xml.MySQL;
import com.bence.mate.spring.xml.Redis;

import org.slf4j.LoggerFactory;
import org.slf4j.Logger;

public class XmlApplication {

	private static Logger logger = LoggerFactory.getLogger(Application.class);

	public static void main(String[] args) {
		// Using Spring without SpringBoots - xml
		ClassPathXmlApplicationContext xmlApplicationContext = new ClassPathXmlApplicationContext("applicationContext.xml");

		Elastic elastic = xmlApplicationContext.getBean(Elastic.class);
		MySQL mysql = xmlApplicationContext.getBean(MySQL.class);
		Mongo mongo = xmlApplicationContext.getBean(Mongo.class);
		Redis redis = xmlApplicationContext.getBean(Redis.class);

		logger.info("{}", elastic.find());
		logger.info("{}", mysql.find());
		logger.info("{}", mongo.find());
		logger.info("{}", redis.find());

		xmlApplicationContext.close();
	}
}
