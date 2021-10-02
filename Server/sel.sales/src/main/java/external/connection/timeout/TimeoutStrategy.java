package external.connection.timeout;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;

public abstract class TimeoutStrategy implements ITimeoutStrategy  {

	private HasTimeout notifyTarget;
	
	private long timeUnitAmount;
	private TemporalUnit timeUnit;
	
	private LocalDateTime startTime;
	private boolean isTerminated = false;
	private boolean isReset = false;
	private boolean isTimerStarted = false;
	
	public TimeoutStrategy(long timeUnitAmount, TemporalUnit timeUnit) {
		this.timeUnitAmount = timeUnitAmount;
		this.timeUnit = timeUnit;
	}

	@Override
	public void setNotifyTarget(HasTimeout notifyTarget) {
		this.notifyTarget = notifyTarget;
	}
	
	@Override
	public void startTimer() {
		this.startTime = LocalDateTime.now();
		this.isReset = false;
		this.isTimerStarted = true;
	}

	@Override
	public void reset() {
		this.startTime = null;
		this.isReset = true;
		this.isTimerStarted = false;
	}

	protected TemporalUnit getTimeUnit() {
		return timeUnit;
	}
	
	protected long getTimeUnitAmount() {
		return timeUnitAmount;
	}

	protected void setTimeUnitAmount(long timeUnitAmount) {
		this.timeUnitAmount = timeUnitAmount;
	}
	@Override
	public boolean isRunning() {
		return !this.isTerminated && this.isTimerStarted && !this.isReset;
	}
	
	@Override
	public boolean isTimeUp() {
		return this.timeUnitAmount <= this.startTime.until(LocalDateTime.now(), this.timeUnit);
	}
	
	@Override
	public void terminate() {
		this.isTerminated = true;
	}
	
	@Override
	public void notifyTarget() {
		if (this.notifyTarget != null) {
			this.notifyTarget.timeout();
		}
	}
	
	@Override
	public void run() {
		while (!this.isTerminated) {
			while (!this.isTimerStarted) {
				
			}
			
			while (!this.isTimeUp()) {
				if (this.isReset) {
					break;
				}
			}
			
			if (!this.isReset) {
				this.notifyTarget();
			}
			this.reset();
		}
	}
}