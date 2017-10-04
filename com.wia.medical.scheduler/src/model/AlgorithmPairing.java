package model;

import java.util.ArrayList;
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
import model.algorithms.IEmployeeGeneratorAlgorithm;
import model.algorithms.ShuffleAlgorithm;
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
		List<Muszak> msz = muszakok.getMuszakok();
		
		IEmployeeGeneratorAlgorithm gen = new ShuffleAlgorithm(separatedEmployees);
		
		for (Muszak m : msz) {

			Set<IJob> missingJobs = m.missingJob();

			for (IJob j : missingJobs) {

				Employee emp = gen.getAnEmployee(j, m);

				if (emp != null) {
					m.addEmployee(emp);
					emp.addMuszak(m);
					
					secondShift(muszakok, m, emp);
				}
			}
		}
	}
	
	private void secondShift(MuszakLista muszakok, Muszak m, Employee emp) { 
		if (emp.isService24h() && !m.isNight()) {
			Muszak m2 = muszakok.getAdjacentAfter(m);
			if (m2 != null) {
				Random r = new Random();
				if (r.nextBoolean() && m2.missingJob().contains(emp.getJob())
						&& !emp.getHolidays().contains(m2.getDay())) {
					m2.addEmployee(emp);
					emp.addMuszak(m2);
				}

			}
		}
	}

}
