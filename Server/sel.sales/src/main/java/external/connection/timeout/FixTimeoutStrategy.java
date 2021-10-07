package external.connection.timeout;

import java.time.temporal.TemporalUnit;
import java.util.concurrent.ExecutorService;

public class FixTimeoutStrategy extends TimeoutStrategy {
	public FixTimeoutStrategy(long timeUnitAmount, TemporalUnit timeUnit, ExecutorService es) {
		super(timeUnitAmount, timeUnit, es);
	}
}
