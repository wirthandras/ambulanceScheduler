package model.components;

import java.util.Random;

public enum ECarType {
	ROHAMKOCSI, ESETKOCSI;

	public static ECarType random() {
		Random r = new Random();
		ECarType[] elements = values();
		int randomizedIndex = r.nextInt(elements.length);
		return elements[randomizedIndex];
	}

}
