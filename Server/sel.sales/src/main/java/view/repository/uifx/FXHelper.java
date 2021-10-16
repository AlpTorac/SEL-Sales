package view.repository.uifx;

import javafx.application.Platform;
import view.repository.IUILibraryHelper;

public class FXHelper implements IUILibraryHelper {
	@Override
	public void queueAsynchroneRunnable(Runnable runnable) {
		Platform.runLater(runnable);
	}
}
