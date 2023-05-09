package com.bence.mate.spring.sort;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

@Service
@Qualifier("bubble")
public class BubbleSortAlgorithm implements SortAlgorithm {
	
	public int[] sort(int[] numbers) {
		return numbers;
	}
}
