package controller;

import javafx.scene.control.TreeItem;

public class Alkalmazottak extends TreeItem<TreeItem<String>> {

	private final static String alk = "Alkalmazottak";

	@Override
	public String toString() {
		return alk;
	}
}
