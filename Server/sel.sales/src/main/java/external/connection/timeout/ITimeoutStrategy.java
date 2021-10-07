package external.connection.timeout;

public interface ITimeoutStrategy {
	void startTimer();
	void reset();
	boolean hasStarted();
	boolean isRunning();
	void setNotifyTarget(HasTimeout notifyTarget);
}
