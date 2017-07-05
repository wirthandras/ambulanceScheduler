package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import model.components.ECarType;
import model.components.Muszak;

public class MuszakLista {

	private int daysInMonth;

	private List<Muszak> muszakok;
	private Map<Integer, Set<Muszak>> dayShifts;

	public MuszakLista(int daysInMonth) {
		muszakok = new ArrayList<Muszak>();
		dayShifts = new HashMap<Integer, Set<Muszak>>();

		this.daysInMonth = daysInMonth;

		for (int i = 1; i <= daysInMonth; i++) {

			Muszak m1 = new Muszak(i, 6, 18, ECarType.ROHAMKOCSI);
			Muszak m2 = new Muszak(i, 7, 19, ECarType.ESETKOCSI);
			Muszak m3 = new Muszak(i, 8, 20, ECarType.ESETKOCSI);
			Muszak m4 = new Muszak(i, 18, 6, ECarType.ROHAMKOCSI);
			Muszak m5 = new Muszak(i, 19, 7, ECarType.ESETKOCSI);
			Muszak m6 = new Muszak(i, 7, 15, ECarType.ESETKOCSI);

			muszakok.add(m1);
			muszakok.add(m2);
			muszakok.add(m3);
			muszakok.add(m4);
			muszakok.add(m5);
			muszakok.add(m6);

			Set<Muszak> set = new HashSet<Muszak>();
			set.add(m1);
			set.add(m2);
			set.add(m3);
			set.add(m4);
			set.add(m5);
			set.add(m6);

			dayShifts.put(i, set);

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

	// TODO improve with next day
	public Muszak getAdjacentAfter(Muszak m) {
		int day = m.getDay();
		int to = m.getTo();

		Set<Muszak> muszakok = dayShifts.get(day);
				
		for (Muszak mt : muszakok) {
			if (mt.getFrom() == to && !m.equals(mt)) {
				return mt;
			}
		}
		return null;

	}

}
