package jobs;

import javafx.scene.control.TreeItem;

public abstract class AbstractJob extends TreeItem<String> implements IJob {

	@Override
	public String toString() {
		return getClass().getSimpleName();
	}
}
