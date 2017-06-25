package jobs;

import model.components.Muszak;

public class Apolo extends AbstractJob {

	@Override
	public boolean accept(Muszak m) {
		return !m.isNight();
	}
	
	@Override
	public String toString() {
		return getClass().getSimpleName();
	}

}
