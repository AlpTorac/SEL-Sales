package external.connection.timeout;

import java.time.temporal.TemporalUnit;

public interface HasTimeout {

	/**
	 * Call when there is a timeout according to {@link TimeoutStrategy}
	 */
	void timeout();

	/**
	 * Call when a timer created by {@link TimeoutStrategy} stops running 
	 */
	void timeoutTimerStopped(boolean wasReset, boolean wasTerminated);
	
	ITimeoutStrategy getTimeoutStrategy();
	
	default void setTimeUnitAmount(long timeUnitAmount) {
		this.getTimeoutStrategy().setTimeUnitAmount(timeUnitAmount);
	}
	default long getTimeUnitAmount() {
		return this.getTimeoutStrategy().getTimeUnitAmount();
	}
	default TemporalUnit getTimeUnit() {
		return this.getTimeoutStrategy().getTimeUnit();
	}
	default void setTimeUnit(TemporalUnit unit) {
		this.getTimeoutStrategy().setTimeUnit(unit);
	}
}