package controller;

import java.util.List;
import java.util.Map;

import javafx.fxml.FXML;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import jobs.AbstractJob;
import jobs.IJob;
import model.Model;
import model.components.Employee;

public class MainController {

	private Model m;

	@FXML
	private TreeView<TreeItem<String>> treeview;

	public void setModel(Model m) {
		this.m = m;

		Alkalmazottak alk = new Alkalmazottak();

		Map<IJob, List<Employee>> map = m.getAlg().getSeparatedEmployees();
		
		for(IJob job : map.keySet()) {
			TreeItem<TreeItem<String>> item = new TreeItem<TreeItem<String>>();
			item.setValue((AbstractJob)job);
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
	}

}
