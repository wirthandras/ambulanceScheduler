package model.algorithms;

import java.time.LocalDate;

import org.junit.Assert;
import org.junit.Test;

import model.algorithms.AbstractAlgorithm;

public class AbstractAlgorithmTest {

	@Test
	public void test() {
		LocalDate date = LocalDate.of(2000, 4, 2);
		AbstractAlgorithm a = new AbstractAlgorithm(date);
		
		int actual = a.getMonthlyRequiredWorkingHours();
		
		Assert.assertEquals(168, actual);
	}
}
