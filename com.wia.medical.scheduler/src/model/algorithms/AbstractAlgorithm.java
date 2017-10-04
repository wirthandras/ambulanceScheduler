package model.algorithms;

import java.time.LocalDate;
import java.time.Month;
import java.util.Calendar;
import java.util.GregorianCalendar;

import model.components.Employee;

public abstract class AbstractAlgorithm {
	
	private static final int oneWorkingDayInHour = 8;
	
	protected LocalDate calculationDate;
	
	public AbstractAlgorithm(LocalDate calculationDate) {
		this.calculationDate = calculationDate;
	}

	private int getMonthlyRequiredWorkingHours() {
		int sumHours = 0;
		boolean isLeapYear = calculationDate.isLeapYear();
		Month m = calculationDate.getMonth();
		int monthLength = m.length(isLeapYear);
		for (int i = 1; i <= monthLength; i++) {
			Calendar mycal = new GregorianCalendar();
			mycal.set(Calendar.DAY_OF_MONTH, i);
			int dayOfWeek = mycal.get(Calendar.DAY_OF_WEEK);
			if(Calendar.SATURDAY != dayOfWeek && Calendar.SUNDAY != dayOfWeek) {
				sumHours += oneWorkingDayInHour;
			}
		}		
		return sumHours;
	}

	protected boolean canAdd(Employee emp) {
		int workingHours = emp.sumWorkingHours();
		int holidayHours = emp.getHolidays().size() * oneWorkingDayInHour;
		int sum = workingHours + holidayHours;
		return sum < getMonthlyRequiredWorkingHours();
	}

}
