package controller;

import java.time.LocalDate;
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
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.layout.Pane;
import javafx.util.Callback;
import jobs.AbstractJob;
import jobs.IJob;
import model.Model;
import model.components.Employee;

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

	public void setModel(Model m) {
		this.m = m;

		Alkalmazottak alk = new Alkalmazottak();

		Map<IJob, List<Employee>> map = m.getAlg().getSeparatedEmployees();

		for (IJob job : map.keySet()) {
			TreeItem<TreeItem<String>> item = new TreeItem<TreeItem<String>>();
			item.setValue((AbstractJob) job);
			alk.getChildren().add(item);
			List<Employee> empList = map.get(job);
			for (Employee e : empList) {
				TreeItem<TreeItem<String>> subItem = new TreeItem<TreeItem<String>>();
				subItem.setValue(e);
				item.getChildren().add(subItem);

			}
		}

		treeview.setRoot(alk);
		treeview.setShowRoot(false);
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

		//
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

}
