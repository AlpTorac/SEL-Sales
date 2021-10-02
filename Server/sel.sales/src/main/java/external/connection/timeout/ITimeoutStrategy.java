package external.connection.timeout;

public interface ITimeoutStrategy extends Runnable {
	void startTimer();
	void reset();
	boolean isTimeUp();
	boolean isRunning();
	void terminate();
	void setNotifyTarget(HasTimeout notifyTarget);
	void notifyTarget();
}
