package external.connection.timeout;

public interface HasTimeout {

	/**
	 * Call when there is a timeout according to {@link TimeoutStrategy}
	 */
	void timeout();

	/**
	 * Call when a timer created by {@link TimeoutStrategy} stops running 
	 */
	void timeoutTimerStopped(boolean wasReset);
	
}