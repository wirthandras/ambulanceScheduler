package model;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

import jobs.IJob;
import jobs.JobFactory;
import model.components.Employee;

public class ReadWorkers {

	public static List<Employee> readEmployees(File file) {
		List<Employee> emps = new ArrayList<>();
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = null;
			IJob job = null;
			while ((line = br.readLine()) != null) {
				if (line.contains(IJob.class.getSimpleName())) {
					String type = line.replace(IJob.class.getSimpleName(), "");
					job = JobFactory.getJob(type);
				} else {					
					emps.add(new Employee(line, job, false));
				}
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return emps;
	}

}
