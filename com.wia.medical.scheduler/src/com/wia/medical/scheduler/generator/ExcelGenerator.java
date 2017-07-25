package com.wia.medical.scheduler.generator;

import java.util.Calendar;
import java.util.Collections;
import java.util.GregorianCalendar;
import java.util.List;
import java.util.Map;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellRangeAddress;

import jobs.IJob;
import jobs.JobFactory;
import model.components.Employee;
import model.components.Muszak;

public class ExcelGenerator {

	public HSSFWorkbook generate(int days, Map<IJob, List<Employee>> employees) {
		HSSFWorkbook workbook = new HSSFWorkbook();

		List<Employee> empMento = employees.get(JobFactory.mento);
		Collections.sort(empMento);
		generateOneSheet(workbook, days, empMento, "tesztMentoTiszt");

		List<Employee> empApolo = employees.get(JobFactory.apolo);
		Collections.sort(empApolo);
		generateOneSheet(workbook, days, empApolo, "tesztApolo");

		List<Employee> empDrivers = employees.get(JobFactory.driver);
		Collections.sort(empDrivers);
		generateOneSheet(workbook, days, empDrivers, "tesztGvk.");

		return workbook;
	}

	private void generateOneSheet(HSSFWorkbook workbook, int days, List<Employee> employees, String sheetName) {
		HSSFSheet sheet = workbook.createSheet(sheetName);

		int actRow = 0;

		createHeaderRow(workbook, sheet, actRow, days);

		actRow++;

		createEmployeeTable(workbook, sheet, actRow, employees, days);

		normalizeBorder(sheet, days);

		// merge names
		mergeCellsForEmployee(sheet, employees.size(), 0);
		// merge holidays
		mergeCellsForEmployee(sheet, employees.size(), days + 1);
		// merge sum
		mergeCellsForEmployee(sheet, employees.size(), days + 2);
	}

	private void normalizeBorder(HSSFSheet sheet, int days) {
		for (int i = 0; i <= days; i++) {
			sheet.autoSizeColumn(i);
		}
	}

	private void createEmployeeTable(HSSFWorkbook workbook, HSSFSheet sheet, int actRow, List<Employee> employees,
			int day) {
		for (int i = 0; i < employees.size(); i++) {

			HSSFRow rowStart = sheet.createRow(actRow);
			actRow++;
			HSSFRow rowFinish = sheet.createRow(actRow);
			actRow++;

			Employee emp = employees.get(i);

			oneRow(workbook, rowStart, emp, day, true);
			oneRow(workbook, rowFinish, emp, day, false);

		}
	}

	private void oneRow(HSSFWorkbook workbook, HSSFRow row, Employee emp, int day, boolean start) {
		for (int j = 0; j <= day + 2; j++) {
			HSSFCell cell = row.createCell(j);

			if (j == 0) {
				cell.setCellValue(emp.getName());
			} else {
				if (j == day + 2) {
					cell.setCellValue(emp.sumWorkingHours());
				} else {
					if (j == day + 1) {
						cell.setCellValue(emp.getHolidays().size() * 8);
					} else {
						if (emp.getSicks().contains(j)) {
							if (!start) {
								cell.setCellValue("B");
							}
						} else {
							if (emp.getHolidays().contains(j)) {
								if (!start) {
									cell.setCellValue("sz");
								}
							} else {
								if (emp.getWorkDays().contains(j)) {
									setCellValue(emp, cell, start, j);
								}
							}
						}
						setCellStyle(workbook, cell, j);
					}
				}
			}

		}
	}

	/**
	 * 
	 * @param emp
	 * @param cell
	 * @param start
	 *            true means arrive cell, false means go home cell.
	 * @param actDay
	 */
	private void setCellValue(Employee emp, HSSFCell cell, boolean start, int actDay) {
		int value = emp.getTime(actDay, start);
		if (value != -1) {
			cell.setCellValue(value);
		}
	}

	private void createHeaderRow(HSSFWorkbook wb, HSSFSheet sheet, int rowNum, int days) {
		HSSFRow row = sheet.createRow(rowNum);
		for (int j = 0; j <= days + 2; j++) {
			HSSFCell cell = row.createCell(j);
			if (j == 0) {
				cell.setCellValue("Nevsor");
			} else {
				if (j == days + 1) {
					cell.setCellValue("Szabi");
				} else {
					if (j == days + 2) {
						cell.setCellValue("Ossz ora");
					} else {
						setCellStyle(wb, cell, j);
						cell.setCellValue(j);
					}
				}
			}
		}
	}

	private CellStyle getSaturdayCellStyle(HSSFWorkbook wb) {
		// Aqua background
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.YELLOW.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return style;
	}

	private CellStyle getSundayCellStyle(HSSFWorkbook wb) {
		// Aqua background
		HSSFCellStyle style = wb.createCellStyle();
		style.setFillForegroundColor(HSSFColor.AQUA.index);
		style.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		return style;
	}

	private void setCellStyle(HSSFWorkbook wb, HSSFCell cell, int day) {

		Calendar mycal = new GregorianCalendar();
		mycal.set(Calendar.DAY_OF_MONTH, day);
		int dayOfWeek = mycal.get(Calendar.DAY_OF_WEEK);

		switch (dayOfWeek) {
		case Calendar.SATURDAY:
			cell.setCellStyle(getSaturdayCellStyle(wb));
			break;
		case Calendar.SUNDAY:
			cell.setCellStyle(getSundayCellStyle(wb));
			break;

		default:
			break;
		}
	}

	private void mergeCellsForEmployee(HSSFSheet sheet, int numberOfEmployees, int column) {
		for (int i = 0; i < numberOfEmployees; i++) {
			int rowIndex = i * 2 + 1;
			sheet.addMergedRegion(new CellRangeAddress(rowIndex, rowIndex + 1, column, column));
		}
	}

	/*
	 * TODO still not working, maybe a bug
	 */
	private void setCenterNames(HSSFWorkbook wb, HSSFSheet sheet) {
		HSSFCellStyle style = wb.createCellStyle();
		style.setAlignment(HorizontalAlignment.CENTER);
		style.setVerticalAlignment(VerticalAlignment.CENTER);
		sheet.setDefaultColumnStyle(1, style);
	}

}
