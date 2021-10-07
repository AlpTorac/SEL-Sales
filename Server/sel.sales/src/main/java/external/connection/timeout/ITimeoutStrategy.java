package external.connection.timeout;

public interface ITimeoutStrategy {
	void startTimer();
	void reset();
	boolean hasRunningTimer();
	boolean hasStarted();
	long getTimeElapsed();
	void setNotifyTarget(HasTimeout notifyTarget);
}
