package model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import jobs.Apolo;
import jobs.Driver;
import jobs.IJob;
import jobs.JobFactory;
import jobs.Mentotiszt;
import model.components.Car;
import model.components.Employee;
import model.components.Muszak;

public class AlgorithmPairing {

	private Map<IJob, List<Employee>> separatedEmployees;

	public void execute(MuszakLista muszakok, List<Car> cars, List<Employee> employees) {

		separateEmployees(employees);

		assignEmpToMuszak(muszakok);

	}

	public Map<IJob, List<Employee>> getSeparatedEmployees() {
		return separatedEmployees;
	}

	private void separateEmployees(List<Employee> employees) {
		separatedEmployees = new HashMap<IJob, List<Employee>>();

		List<Employee> apolok = new ArrayList<>();
		List<Employee> drivers = new ArrayList<>();
		List<Employee> medics = new ArrayList<>();

		for (Employee e : employees) {

			if (e.getJob() instanceof Driver) {
				drivers.add(e);
			}
			if (e.getJob() instanceof Apolo) {
				apolok.add(e);
			}
			if (e.getJob() instanceof Mentotiszt) {
				medics.add(e);
			}

			separatedEmployees.put(JobFactory.apolo, apolok);
			separatedEmployees.put(JobFactory.driver, drivers);
			separatedEmployees.put(JobFactory.mento, medics);

		}
	}

	private void assignEmpToMuszak(MuszakLista muszakok) {
		for (Muszak m : muszakok.getMuszakok()) {

			Set<IJob> missingJobs = m.missingJob();

			for (IJob j : missingJobs) {
				Employee emp = getAnEmployee(j, m);
				if (emp != null) {
					m.addEmployee(emp);
					emp.addMuszak(m);
				}
			}
		}
	}

	private Employee getAnEmployee(IJob j, Muszak m) {
		List<Employee> emps = separatedEmployees.get(j);
		List<Employee> employees = new ArrayList<>(emps);

		if (employees != null && employees.size() > 0) {

			Random r = new Random();
			int rValue = r.nextInt(30);

			Collections.shuffle(employees, new Random(rValue));

			Employee emp = getFirstNotOnHoliday(m, employees);

			return emp;
		} else {
			return null;
		}
	}

	private Employee getFirstNotOnHoliday(Muszak m, List<Employee> employees) {
		int i = 0;
		while (i < employees.size()) {
			Employee emp = employees.get(i);

			if (emp.canAdd(m)) {
				return emp;
			}

			i++;
		}
		return null;
	}

}
