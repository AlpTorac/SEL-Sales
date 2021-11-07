package external.connection.timeout;

import java.time.temporal.TemporalUnit;

public interface ITimeoutStrategy {
	void terminateTimer();
	void startTimer();
	void reset();
	boolean hasRunningTimer();
	boolean hasStarted();
	long getTimeElapsed();
	void setNotifyTarget(HasTimeout notifyTarget);
	void setTimeUnitAmount(long timeUnitAmount);
	long getTimeUnitAmount();
	TemporalUnit getTimeUnit();
	void setTimeUnit(TemporalUnit unit);
}
