package jobs;

import model.components.Muszak;

public class Driver extends AbstractJob {

	/**
	 * Maximal working time in Hour.
	 */
	protected static final int maximalWorkingTime = 12;

	@Override
	public boolean accept(Muszak m) {
		if (m.duration() <= 12) {
			return true;
		} else {
			return false;
		}
	}

}
