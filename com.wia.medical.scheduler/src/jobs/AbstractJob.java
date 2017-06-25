package jobs;

public abstract class AbstractJob implements IJob {

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
