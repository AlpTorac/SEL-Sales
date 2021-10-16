package view.repository;

public interface IUILibraryHelper {
	void queueAsynchroneRunnable(Runnable runnable);
	void setImplicitExit(boolean exitOnClose);
}
