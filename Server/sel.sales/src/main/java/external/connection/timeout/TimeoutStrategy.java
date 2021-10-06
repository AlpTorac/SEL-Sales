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
	public boolean hasStarted() {
		return this.isTimerStarted;
	}
	
	@Override
	public void setNotifyTarget(HasTimeout notifyTarget) {
		System.out.println("notify target hash: " + notifyTarget);
		this.notifyTarget = notifyTarget;
	}
	
	@Override
	public void startTimer() {
		this.startTime = LocalDateTime.now();
		this.isReset = false;
		this.isTimerStarted = true;
		System.out.println("startTimer() in " + notifyTarget);
	}

	@Override
	public void reset() {
		this.startTime = null;
		this.isReset = true;
		System.out.println("reset() in " + notifyTarget);
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
		System.out.println("Timer starting to run: " + notifyTarget);
		boolean started = false;
		boolean timeUp = false;
		while (!this.isTerminated) {
			System.out.println("Awaiting timer start: " + notifyTarget);
			
			while (!started) {
				started = this.hasStarted();
			}
			
			System.out.println("Timer started: " + notifyTarget);
			
			while (!timeUp) {
				timeUp = this.isTimeUp();
				if (this.isReset) {
					System.out.println("Timer reset by notify target: " + notifyTarget);
					break;
				}
			}
			
			System.out.println("TimeUp/Reset: " + notifyTarget);
			
			if (!this.isReset) {
				System.out.println("Notifying target: " + this.notifyTarget);
				this.notifyTarget();
			}
			
			this.reset();
			this.isTimerStarted = false;
			System.out.println("Cycle over: " + notifyTarget);
		}
		System.out.println("Timer terminated: " + notifyTarget);
	}
}