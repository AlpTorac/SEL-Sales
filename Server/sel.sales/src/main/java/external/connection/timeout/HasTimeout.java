package external.connection.timeout;

public interface HasTimeout {

	/**
	 * Perform the needed actions when no acknowledgement has been received during the acknowledgement timer.
	 */
	void timeout();

}