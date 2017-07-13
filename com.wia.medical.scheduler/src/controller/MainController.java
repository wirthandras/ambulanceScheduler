package controller;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.sun.javafx.scene.control.skin.DatePickerSkin;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import jobs.AbstractJob;
import jobs.IJob;
import jobs.JobFactory;
import model.Model;
import model.MuszakLista;
import model.components.Car;
import model.components.Day;
import model.components.Employee;
import model.components.Muszak;

public class MainController {

	private Employee selectedEmployee;

	private Stage primaryStage;
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

	@FXML
	private ChoiceBox<AbstractJob> choiceJobs;

	@FXML
	private TextField inputEmployeeName;

	@FXML
	private CheckBox checkBox24hInput;

	public void setData(Model m, Stage primaryStage) {
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
				if (treeItem != null && treeItem.getValue() instanceof Employee) {
					selectedEmployee = (Employee) treeItem.getValue();
					setupGuiAccordingToEmployee(selectedEmployee);
				}
			}

		});
	}

	@FXML
	public void initialize() {
		picker = new DatePicker(LocalDate.now());
		datePickerSkin = new DatePickerSkin(picker);

		ObservableList<AbstractJob> jobs = FXCollections.observableArrayList(JobFactory.apolo, JobFactory.doctor,
				JobFactory.mento, JobFactory.driver, JobFactory.szakapolo);

		choiceJobs.setItems(jobs);
	}

	private void setupGuiAccordingToEmployee(Employee emp) {
		h24.setSelected(emp.isService24h());

		Set<Integer> holidays = emp.getHolidays();
		Set<Integer> workDays = emp.getWorkDays();
		Set<Integer> sicks = emp.getSicks();

		picker.setDayCellFactory(createFactory(workDays, holidays, sicks));

		pane.getChildren().clear();

		Node popupContent = datePickerSkin.getPopupContent();

		pane.getChildren().add(popupContent);
	}

	private Callback<DatePicker, DateCell> createFactory(Set<Integer> workDays, Set<Integer> holidays, Set<Integer> sickDays) {

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
							} else {
								if(sickDays.contains(item.getDayOfMonth())) {
									setStyle("-fx-background-color: #00FA9A;");
								}
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
		try {
			m.save();
		} catch (FileNotFoundException e) {
			showPopup("Fájl meg van nyitva, kérem csukja be!");
		} catch (IOException e) {
			showPopup("Fájl hiba");
		}
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
		for (TreeItem<TreeItem<String>> item : treeviewShifts.getRoot().getChildren()) {
			expand(item, false);
		}
	}

	@FXML
	private void pickUpNewEmployee() {
		String empName = inputEmployeeName.getText();
		if (!empName.equals("")) {
			IJob job = choiceJobs.getValue();
			if (job != null) {
				boolean is24h = checkBox24hInput.isSelected();
				Employee emp = new Employee(empName, job, is24h);
				m.addNewEmployee(emp);

				// redraw employee tree
				drawTreeView();
			} else {
				showPopup("Kérem válasszon munkát!");
			}
		} else {
			showPopup("Név nincs kitöltve");
		}
	}

	private void showPopup(String message) {
		final Stage dialog = new Stage();
		dialog.initModality(Modality.APPLICATION_MODAL);
		dialog.initOwner(this.primaryStage);
		VBox dialogVbox = new VBox(20);
		dialogVbox.getChildren().add(new Text(message));
		Scene dialogScene = new Scene(dialogVbox, 300, 200);
		dialog.setScene(dialogScene);
		dialog.show();
	}

	@FXML
	private void pickUpNewHoliday() {

		LocalDate date = datePickerSkin.getBehavior().getControl().getValue();
		int day = date.getDayOfMonth();

		selectedEmployee.getHolidays().add(day);
		setupGuiAccordingToEmployee(selectedEmployee);
	}

	@FXML
	private void h24TickBoxPressed() {
		boolean is24hSelected = h24.isSelected();
		selectedEmployee.setService24h(is24hSelected);
	}

	@FXML
	private void pickupSickDay() {

		LocalDate date = datePickerSkin.getBehavior().getControl().getValue();
		int day = date.getDayOfMonth();

		selectedEmployee.getSicks().add(day);
		setupGuiAccordingToEmployee(selectedEmployee);
	}

}
