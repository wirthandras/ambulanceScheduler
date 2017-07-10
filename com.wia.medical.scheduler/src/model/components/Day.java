package model.components;

import javafx.scene.control.TreeItem;

public class Day extends TreeItem<String> {

	private int day;

	public Day(int day) {
		this.day = day;
	}

	public int getDay() {
		return day;
	}
	
	@Override
	public String toString() {
		return day + ". nap";
	}
}
