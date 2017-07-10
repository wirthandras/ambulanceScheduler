package controller;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.CheckBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeTableView;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import jobs.AbstractJob;
import jobs.IJob;
import model.Model;
import model.MuszakLista;
import model.components.Car;
import model.components.Day;
import model.components.Employee;
import model.components.Muszak;

public class MainController {

	private Model m;

	@FXML
	private TreeView<TreeItem<String>> treeview;
	@FXML
	private CheckBox h24;
	@FXML
	private Pane pane;

	private DatePickerSkin datePickerSkin;

	private DatePicker picker;

	@FXML
	private TableView<Car> carTable;

	@FXML
	private TreeView<TreeItem<String>> treeviewShifts;

	public void setModel(Model m) {
		this.m = m;

		drawTreeView();
		drawTreeViewShifts();
		drawTable();
		//
		addListeners();
	}

	private void drawTable() {

		TableColumn<Car, String> firstNameCol = new TableColumn<Car, String>("ID");
		firstNameCol.setCellValueFactory(new PropertyValueFactory<Car, String>("identifier"));
		TableColumn<Car, String> secondNameCol = new TableColumn<Car, String>("Type");
		secondNameCol.setCellValueFactory(new PropertyValueFactory<Car, String>("typeText"));
		carTable.getColumns().clear();
		carTable.getColumns().add(firstNameCol);
		carTable.getColumns().add(secondNameCol);
		carTable.getItems().clear();
		carTable.getItems().addAll(m.getCars());
	}

	private void drawTreeView() {
		treeview.setShowRoot(false);

		Alkalmazottak alk = new Alkalmazottak();

		List<Employee> employees = m.getEmployees();

		Map<IJob, List<Employee>> employeesMap = new HashMap<IJob, List<Employee>>();
		for (Employee e : employees) {
			List<Employee> subList = employeesMap.get(e.getJob());
			if (subList != null) {
				subList.add(e);
			} else {
				List<Employee> emps = new ArrayList<Employee>();
				emps.add(e);
				employeesMap.put(e.getJob(), emps);
			}

		}

		for (IJob job : employeesMap.keySet()) {
			TreeItem<TreeItem<String>> item = new TreeItem<TreeItem<String>>();
			item.setValue((AbstractJob) job);
			alk.getChildren().add(item);
			List<Employee> empList = employeesMap.get(job);
			for (Employee e : empList) {
				TreeItem<TreeItem<String>> subItem = new TreeItem<TreeItem<String>>();
				subItem.setValue(e);
				item.getChildren().add(subItem);

			}
		}

		treeview.setRoot(alk);
	}

	private void drawTreeViewShifts() {
		treeviewShifts.setShowRoot(false);

		Alkalmazottak alk = new Alkalmazottak();

		MuszakLista shifts = m.getMuszakLista();

		Map<Integer, List<Muszak>> shiftList = new HashMap<Integer, List<Muszak>>();

		for (Muszak shift : shifts.getMuszakok()) {
			List<Muszak> subList = shiftList.get(shift.getDay());
			if (subList != null) {
				subList.add(shift);
			} else {
				subList = new ArrayList<Muszak>();
				subList.add(shift);
				shiftList.put(shift.getDay(), subList);
			}

		}

		for (Integer day : shiftList.keySet()) {
			TreeItem<TreeItem<String>> item = new TreeItem<TreeItem<String>>();
			item.setValue(new Day(day));
			alk.getChildren().add(item);
			List<Muszak> subShiftList = shiftList.get(day);
			for (Muszak e : subShiftList) {
				TreeItem<TreeItem<String>> subItem = new TreeItem<TreeItem<String>>();
				subItem.setValue(e);
				item.getChildren().add(subItem);

			}
		}

		treeviewShifts.setRoot(alk);
	}

	private void addListeners() {
		treeview.getSelectionModel().selectedItemProperty().addListener(new ChangeListener() {

			@Override
			public void changed(ObservableValue observable, Object oldValue, Object newValue) {
				TreeItem<TreeItem<String>> treeItem = (TreeItem<TreeItem<String>>) newValue;
				if (treeItem.getValue() instanceof Employee) {
					Employee emp = (Employee) treeItem.getValue();
					setupGuiAccordingToEmployee(emp);
				}

			}

		});
	}

	@FXML
	public void initialize() {
		picker = new DatePicker(LocalDate.now());
		datePickerSkin = new DatePickerSkin(picker);

	}

	private void setupGuiAccordingToEmployee(Employee emp) {
		h24.setSelected(emp.isService24h());

		Set<Integer> holidays = emp.getHolidays();
		Set<Integer> workDays = emp.getWorkDays();

		picker.setDayCellFactory(createFactory(workDays, holidays));

		pane.getChildren().clear();

		Node popupContent = datePickerSkin.getPopupContent();

		pane.getChildren().add(popupContent);
	}

	private Callback<DatePicker, DateCell> createFactory(Set<Integer> workDays, Set<Integer> holidays) {

		Callback<DatePicker, DateCell> dayCellFactory = new Callback<DatePicker, DateCell>() {
			public DateCell call(final DatePicker datePicker) {
				return new DateCell() {
					@Override
					public void updateItem(LocalDate item, boolean empty) {
						super.updateItem(item, empty);
						if (holidays.contains(item.getDayOfMonth())) {
							setStyle("-fx-background-color: #ff4444;");
						} else {
							if (workDays.contains(item.getDayOfMonth())) {
								setStyle("-fx-background-color: #ADD8E6;");
							}
						}

					}
				};
			}
		};
		return dayCellFactory;
	}

	@FXML
	private void save() {
		m.save();
	}

	@FXML
	private void generate() {
		m.generate();
	}

	@FXML
	private void clear() {
		m.clear();
	}

	@FXML
	private void openAll() {
		expand(treeviewShifts.getRoot(), true);
	}

	private void expand(TreeItem<TreeItem<String>> item, boolean expand) {
		item.setExpanded(expand);
		for (TreeItem<TreeItem<String>> subItem : item.getChildren()) {
			expand(subItem, expand);
		}
	}

	@FXML
	private void closeAll() {
		for(TreeItem<TreeItem<String>> item : treeviewShifts.getRoot().getChildren()) {
			expand(item, false);
		}
	}

}
