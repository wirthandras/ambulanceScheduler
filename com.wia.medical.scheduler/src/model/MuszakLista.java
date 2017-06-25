package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import model.components.ECarType;
import model.components.Muszak;

public class MuszakLista {

	private int daysInMonth;

	private List<Muszak> muszakok;

	public MuszakLista(int daysInMonth) {
		muszakok = new ArrayList<Muszak>();

		this.daysInMonth = daysInMonth;

		for (int i = 1; i <= daysInMonth; i++) {
			muszakok.add(new Muszak(i, 6, 18, ECarType.ROHAMKOCSI));
			muszakok.add(new Muszak(i, 7, 19, ECarType.ESETKOCSI));
			muszakok.add(new Muszak(i, 8, 20, ECarType.ESETKOCSI));
			muszakok.add(new Muszak(i, 18, 6, ECarType.ROHAMKOCSI));
			muszakok.add(new Muszak(i, 19, 7, ECarType.ESETKOCSI));
			muszakok.add(new Muszak(i, 7, 15, ECarType.ESETKOCSI));
		}

	}

	@Override
	public String toString() {
		return Arrays.toString(muszakok.toArray());
	}

	public List<Muszak> getMuszakok() {
		return muszakok;
	}

	public int getDaysInMonth() {
		return daysInMonth;
	}

}
