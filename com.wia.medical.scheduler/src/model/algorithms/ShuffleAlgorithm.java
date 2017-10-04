package model.algorithms;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;

import jobs.IJob;
import model.components.Employee;
import model.components.Muszak;

public class ShuffleAlgorithm extends AbstractAlgorithm implements IEmployeeGeneratorAlgorithm {
	
	private Map<IJob, List<Employee>> separatedEmployees;
	
	public ShuffleAlgorithm(Map<IJob, List<Employee>> separatedEmployees, LocalDate calculationDate) {
		super(calculationDate);
		this.separatedEmployees = separatedEmployees;
	}
	
	@Override
	public Employee getAnEmployee(IJob j, Muszak m) {
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
			
			if (emp.canAdd(m) && canAdd(emp)) {
				return emp;
			}

			i++;
		}
		return null;
	}
}
