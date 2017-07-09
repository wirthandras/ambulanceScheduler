package model.components;

import java.util.Random;

public enum ECarType {
	ROHAMKOCSI("Rohamkocsi"), ESETKOCSI("Esetkocsi");

	private String name;

	private ECarType(String name) {
		this.name = name;
	}

	public static ECarType random() {
		Random r = new Random();
		ECarType[] elements = values();
		int randomizedIndex = r.nextInt(elements.length);
		return elements[randomizedIndex];
	}

	@Override
	public String toString() {
		return name;
	}

}
