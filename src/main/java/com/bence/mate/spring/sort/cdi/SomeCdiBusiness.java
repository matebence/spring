package com.bence.mate.spring.sort.cdi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class SomeCdiBusiness {

	@Autowired
	private SomeCdiDao someCdiDao;

	public void setSomeCDIDAO(SomeCdiDao someCdiDao) {
		this.someCdiDao = someCdiDao;
	}

	public int findGreatest() {
		int greatest = Integer.MIN_VALUE;
		int[] data = someCdiDao.getData();
		for (int value : data) {
			if (value > greatest) {
				greatest = value;
			}
		}
		return greatest;
	}
}
