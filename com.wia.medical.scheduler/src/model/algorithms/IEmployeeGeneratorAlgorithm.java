package model.algorithms;

import jobs.IJob;
import model.components.Employee;
import model.components.Muszak;

public interface IEmployeeGeneratorAlgorithm {

	public Employee getAnEmployee(IJob j, Muszak m);
	
}
