package model.components;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javafx.scene.control.TreeItem;
import jobs.IJob;

public class Muszak extends TreeItem<String> implements Comparable<Muszak> {

	/**
	 * Day in Month according to Gregorian calendar.
	 */
	private int day;
	private int from;
	private int to;
	private Map<IJob, Employee> emps;
	private ECarType carType;

	public Muszak(int day, int from, int to, ECarType carType) {
		super();
		this.day = day;
		this.from = from;
		this.to = to;
		this.carType = carType;
	}

	public int getDay() {
		return day;
	}

	public int getFrom() {
		return from;
	}

	public int getTo() {
		return to;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(" Müszak[" + from + "->" + to + "]");
		return sb.toString();
	}

	public void addEmployee(Employee emp) {
		if (emps == null) {
			emps = new HashMap<IJob, Employee>();
		}
		emps.put(emp.getJob(), emp);
	}

	public boolean hasJob(IJob j) {
		return emps.containsKey(j);
	}

	public Set<IJob> missingJob() {
		Set<IJob> jobs = CarRequiredJobs.get(carType);

		if (emps != null) {
			jobs.removeAll(emps.keySet());
		}
		return jobs;
	}

	@Override
	public int compareTo(Muszak m) {
		return Integer.compare(day, m.getDay());
	}

	public boolean isNight() {
		return to - from < 0;
	}

	public int duration() {
		if (from > to) {
			return from - to;
		} else {
			return to - from;
		}
	}

	public void clearAllEmp() {
		emps.clear();
	}

}
