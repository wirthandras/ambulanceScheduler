package model;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

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

	public AlgorithmPairing getAlg() {
		return alg;
	}

	public Model() {

		Calendar mycal = new GregorianCalendar();

		daysInMonth = mycal.getActualMaximum(Calendar.DAY_OF_MONTH);

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

	private void randomizeHolidays() {
		int empSize = employees.size();

		Random r = new Random();
		int rHolidayForEmp = r.nextInt(empSize) / 3;
		Collections.shuffle(employees);
		for (int i = 0; i < rHolidayForEmp; i++) {
			Employee emp = employees.get(i);

			Set<Integer> holidays = new HashSet<Integer>();
			int rHolidaysNumber = r.nextInt(3);
			for (int j = 0; j < rHolidaysNumber; j++) {
				int day = r.nextInt(muszakok.getDaysInMonth()) + 1;
				holidays.add(day);
			}
			emp.addHolidays(holidays);
		}
	}

	private void createExcelFile(int daysInMonth, Map<IJob, List<Employee>> employees) {
		ExcelGenerator gen = new ExcelGenerator();
		HSSFWorkbook workbook = gen.generate(daysInMonth, employees);
		try {
			workbook.write(new File("tesztBeosztas2.xls"));
		} catch (IOException e) {
			e.printStackTrace();
		}
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
		randomizeHolidays();

		alg = new AlgorithmPairing();

		for (Employee e : employees) {
			e.clearShifts();
		}

		for (Muszak m : muszakok.getMuszakok()) {
			m.clearAllEmp();
		}
	}

	public void generate() {
		randomizeHolidays();

		alg = new AlgorithmPairing();
		alg.execute(muszakok, cars, employees);
	}

	public void save() {
		createExcelFile(daysInMonth, alg.getSeparatedEmployees());
	}

	public MuszakLista getMuszakLista() {
		return muszakok;
	}

}
