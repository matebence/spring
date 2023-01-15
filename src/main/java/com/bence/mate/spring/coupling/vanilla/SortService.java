package com.bence.mate.spring.coupling.vanilla;

public class SortService {

	private SortAlgorithm sortAlgorithm;
	
	public SortService(SortAlgorithm sortAlgorithm) {
		this.sortAlgorithm = sortAlgorithm;
	}
	
	public int[] sort(int[] numbers) {
		return this.sortAlgorithm.sort(numbers);
	}
}
