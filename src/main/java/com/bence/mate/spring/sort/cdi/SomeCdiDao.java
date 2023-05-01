package com.bence.mate.spring.sort.cdi;

import org.springframework.stereotype.Repository;

@Repository
public class SomeCdiDao {
	
	public int[] getData() {
		return new int[] {5, 89,100};
	}
}
