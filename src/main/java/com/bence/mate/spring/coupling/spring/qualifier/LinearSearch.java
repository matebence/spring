package com.bence.mate.spring.coupling.spring.qualifier;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

@Component
@Qualifier(value = "linear")
public class LinearSearch implements SearchAlgorithm {

	@Override
	public boolean found(int[] haystack, int needle) {
		return true;
	}
}
