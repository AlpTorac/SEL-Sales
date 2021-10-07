package external.connection.timeout;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.ExecutorService;

public class PingPongTimeoutStrategy extends TimeoutStrategy {

	protected long minimalTimeToWait;
	
	public PingPongTimeoutStrategy(long timeUnitAmount, TemporalUnit timeUnit, ExecutorService es, long minimalTimeToWait) {
		super(timeUnitAmount, timeUnit, es);
		this.minimalTimeToWait = minimalTimeToWait;
	}
	
	@Override
	protected PingPongTimeoutTimer createTimer() {
		return new PingPongTimeoutTimer(LocalDateTime.now(), this.getTimeUnitAmount(), this.getTimeUnit(), this.getNotifyTarget(), this.minimalTimeToWait);
	}
	
	protected class PingPongTimeoutTimer extends TimeoutTimer {
		private final long minimalTimeToWait;
		
		protected PingPongTimeoutTimer(LocalDateTime startTime, long timeUnitAmount, TemporalUnit timeUnit,
				HasTimeout notifyTarget, long minimalTimeToWait) {
			super(startTime, timeUnitAmount, timeUnit, notifyTarget);
			this.minimalTimeToWait = minimalTimeToWait;
		}
		
		@Override
		protected void waitTillTimeIsUp() {
			while (!this.isTimeUp()) {
				if (this.isReset()) {
//					System.out.println(this + " Timer reset by notify target: " + this.getNotifyTarget());
					break;
				}
			}
			
			while (this.getTimeElapsed() < this.minimalTimeToWait) {
				
			}
		}
	}
	
}
