package external.buffer;

import java.time.temporal.TemporalUnit;

public class FixTimeoutStrategy extends TimeoutStrategy {
	
	public FixTimeoutStrategy(long timeUnitAmount, TemporalUnit timeUnit) {
		super(timeUnitAmount, timeUnit);
	}

}
