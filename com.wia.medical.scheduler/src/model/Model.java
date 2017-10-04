package model;

import java.io.File;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.wia.medical.scheduler.generator.ExcelGenerator;

import jobs.IJob;
import model.components.Car;
import model.components.ECarType;
import model.components.Employee;
import model.components.Muszak;

public class Model {

	private MuszakLista muszakok;
	private List<Car> cars;
	private List<Employee> employees;
	private AlgorithmPairing alg;
	private int daysInMonth;
	private LocalDate date;

	public AlgorithmPairing getAlg() {
		return alg;
	}

	public Model() {

		date = LocalDate.now();

		boolean leapYear = date.isLeapYear();
		daysInMonth = date.getMonth().length(leapYear);

		cars = new ArrayList<>();

		generateRealCars();

		employees = ReadWorkers.readEmployees(new File("workers"));
		muszakok = new MuszakLista(daysInMonth);
	}

	private void generateRealCars() {

		cars.add(new Car("HHH-001", ECarType.ESETKOCSI));
		cars.add(new Car("HHH-002", ECarType.ESETKOCSI));
		cars.add(new Car("HHH-003", ECarType.ESETKOCSI));
		cars.add(new Car("HHH-004", ECarType.ROHAMKOCSI));
		cars.add(new Car("HHH-001", ECarType.ESETKOCSI));
		cars.add(new Car("HHH-002", ECarType.ESETKOCSI));
	}

	public List<Car> getCars() {
		return cars;
	}

	@Override
	public String toString() {

		StringBuilder sb = new StringBuilder();
		sb.append(Arrays.toString(cars.toArray()));
		sb.append("\n");
		sb.append(Arrays.toString(employees.toArray()));
		sb.append("\n");
		sb.append("\n");
		sb.append(muszakok.toString());

		return sb.toString();
	}

	private void createExcelFile(int daysInMonth, Map<IJob, List<Employee>> employees) throws IOException {
		File file = new File("tesztBeosztas2.xls");

		ExcelGenerator gen = new ExcelGenerator();
		HSSFWorkbook workbook = gen.generate(daysInMonth, employees);
		workbook.write(file);
	}

	public List<Employee> getEmployees() {
		return employees;
	}

	public Employee getEmployee(String name) {
		for (Employee e : employees) {
			if (e.getName().equals(name)) {
				return e;
			}
		}
		return null;
	}

	public void clear() {

		alg = new AlgorithmPairing();

		for (Employee e : employees) {
			e.clearShifts();
		}

		for (Muszak m : muszakok.getMuszakok()) {
			m.clearAllEmp();
		}
	}

	public void generate() {
		alg = new AlgorithmPairing();
		alg.execute(muszakok, cars, employees, date);
	}

	public void save() throws IOException {
		createExcelFile(daysInMonth, alg.getSeparatedEmployees());
	}

	public MuszakLista getMuszakLista() {
		return muszakok;
	}

	public void addNewEmployee(Employee emp) {
		employees.add(emp);
	}

}
