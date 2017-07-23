package model.components;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.TreeItem;
import jobs.IJob;

public class Employee extends TreeItem<String> implements Comparable<Employee> {

	private static final int maxInMonth = 168;
	private static final int holidayInHour = 8;

	private String name;
	private IJob job;
	private boolean service24h;
	private Set<Muszak> muszakok;
	private Set<Integer> holidays;
	private Set<Integer> sicks;

	public Employee(String name, IJob qualification, boolean service24h) {
		super();
		this.name = name;
		this.job = qualification;
		this.service24h = service24h;

		this.muszakok = new HashSet<Muszak>();
		this.holidays = new HashSet<Integer>();
		this.sicks = new HashSet<Integer>();
	}

	public String getName() {
		return name;
	}

	public IJob getJob() {
		return job;
	}

	@Override
	public String toString() {
		return name;
	}

	public boolean isService24h() {
		return service24h;
	}

	public void setService24h(boolean service24h) {
		this.service24h = service24h;
	}

	public void addMuszak(Muszak m) {
		muszakok.add(m);
	}

	public Set<Muszak> getMuszakok() {
		return muszakok;
	}

	public Map<Integer, List<Muszak>> getMuszakokMap() {
		Map<Integer, List<Muszak>> map = new HashMap<Integer, List<Muszak>>();
		for (Muszak m : muszakok) {

			int day = m.getDay();

			if (map.get(day) != null) {
				map.get(day).add(m);
			} else {
				List<Muszak> list = new ArrayList<Muszak>();
				list.add(m);
				map.put(day, list);
			}
		}
		return map;
	}

	public Set<Integer> getWorkDays() {
		Set<Integer> ms = new HashSet<Integer>();
		for (Muszak m : muszakok) {
			ms.add(m.getDay());
			if (m.isNight()) {
				ms.add(m.getDay() + 1);
			}
		}
		return ms;
	}

	public String printMuszakok() {
		List<Muszak> ordered = new ArrayList<Muszak>(muszakok);
		Collections.sort(ordered);
		return ordered.toString();
	}

	public String printHolidays() {
		return holidays.toString();
	}

	public void addHolidays(Set<Integer> holidays) {
		this.holidays = holidays;
	}

	public Set<Integer> getHolidays() {
		return holidays;
	}

	public Set<Integer> getSicks() {
		return sicks;
	}

	public void setSicks(Set<Integer> sicks) {
		this.sicks = sicks;
	}

	public boolean canAdd(Muszak uj) {

		if (!job.accept(uj)) {
			return false;
		}

		// TODO check next day too in case of night shift
		if (!getHolidays().contains(uj.getDay()) && !getSicks().contains(uj.getDay())) {

			List<Muszak> ordered = new ArrayList<Muszak>(muszakok);

			if (ordered.size() > 0) {

				Collections.sort(ordered, Comparator.reverseOrder());

				Muszak utolso = ordered.get(0);

				if (utolso.getDay() == uj.getDay()) {
					return false;

				} else {
					return diff(uj, utolso);
				}

			} else {
				return checkMaxInMonth();
			}
		} else {
			return false;
		}
	}

	private boolean diff(Muszak uj, Muszak utolso) {

		if (!checkSunday(uj, utolso)) {
			return false;
		} else {

			int minimalPause = 12;

			if (uj.getDay() > utolso.getDay() + 1) {
				return checkMaxInMonth();
			} else {
				if (uj.getDay() == utolso.getDay() + 1) {

					int val = (utolso.getTo() + minimalPause) % 24;

					if (uj.getFrom() >= val) {
						return checkMaxInMonth();
					} else {
						return false;
					}

				} else {
					return false;
				}
			}
		}
	}

	// TODO tmp
	private boolean checkMaxInMonth() {
		int sum = sumWorkingHours();
		int holidayHours = getHolidays().size() * holidayInHour;
		return sum + holidayHours < maxInMonth;
	}

	public int sumWorkingHours() {
		int sum = 0;
		for (Muszak m : muszakok) {
			sum += m.duration();
		}
		return sum;
	}

	private boolean checkSunday(Muszak uj, Muszak utolso) {

		if (uj.getDay() - utolso.getDay() == 1) {

			Calendar mycal = new GregorianCalendar();
			mycal.set(Calendar.DAY_OF_MONTH, utolso.getDay());

			return mycal.get(Calendar.DAY_OF_WEEK) != Calendar.SUNDAY;
		} else {
			return true;
		}
	}

	@Override
	public int compareTo(Employee other) {
		return this.name.compareTo(other.getName());
	}

	public void clearShifts() {
		muszakok.clear();
	}

	public int getTime(int day, boolean start) {

		List<Muszak> actDayShifts = getMuszakokMap().get(day);
		if (start) {
			if (actDayShifts != null && actDayShifts.size() > 0) {
				Collections.sort(actDayShifts);
				Muszak m = actDayShifts.get(0);
				return m.getFrom();
			}
		} else {
			if (actDayShifts != null && actDayShifts.size() > 0) {
				Collections.sort(actDayShifts, Comparator.reverseOrder());
				Muszak m = actDayShifts.get(0);
				if (m.isNight()) {
					switch (actDayShifts.size()) {
					case 2:
						return 24;
					case 1:
					case 0:
						List<Muszak> previousDay = getMuszakokMap().get(day - 1);
						if (previousDay != null) {
							Collections.sort(previousDay, Comparator.reverseOrder());
							Muszak previusShift = previousDay.get(0);
							if (previusShift.isNight()) {
								return previusShift.getTo();
							}
						}
						break;
					default:
						break;
					}
				} else {
					return m.getTo();
				}
			} else {
				List<Muszak> previousDay = getMuszakokMap().get(day - 1);
				if (previousDay != null) {
					Collections.sort(previousDay, Comparator.reverseOrder());
					Muszak previusShift = previousDay.get(0);
					if (previusShift.isNight()) {
						return previusShift.getTo();
					}
				}
			}
		}
		return -1;
	}

	public void addSpecialShift(LocalDate date, int from, int to) {
		if (date != null) {
			int day = date.getDayOfMonth();

			Muszak m = new Muszak(day, from, to, null);

			addMuszak(m);
		}
	}
}
