package external.connection.timeout;

import java.time.LocalDateTime;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

public abstract class TimeoutStrategy implements ITimeoutStrategy  {

	private ExecutorService es;
	
	private HasTimeout notifyTarget;
	
	private long timeUnitAmount;
	private TemporalUnit timeUnit;
	
	private TimeoutTimer timer;
	
	public TimeoutStrategy(long timeUnitAmount, TemporalUnit timeUnit, ExecutorService es) {
		this.timeUnitAmount = timeUnitAmount;
		this.timeUnit = timeUnit;
		this.es = es;
	}

	@Override
	public void terminateTimer() {
		if (this.timer != null) {
			this.timer.isRunning = false;
		}
	}
	
	@Override
	public boolean hasStarted() {
		return this.timer != null;
	}
	
	@Override
	public void setNotifyTarget(HasTimeout notifyTarget) {
//		System.out.println(this + " notify target hash: " + notifyTarget);
		this.notifyTarget = notifyTarget;
	}
	
	@Override
	public void startTimer() {
		if (!this.hasRunningTimer()) {
			this.timer = this.createTimer();
			this.es.submit(this.timer);
		}
	}

	@Override
	public void reset() {
		if (this.timer != null) {
			this.timer.reset();
		}
//		System.out.println(this + " reset() in " + notifyTarget);
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
	
	protected TimeoutTimer createTimer() {
		return new TimeoutTimer(LocalDateTime.now(), this.getTimeUnitAmount(), this.getTimeUnit(), this.notifyTarget);
	}
	
	protected HasTimeout getNotifyTarget() {
		return this.notifyTarget;
	}
	
	@Override
	public boolean hasRunningTimer() {
		return this.timer != null && this.timer.isRunning();
	}
	
	@Override
	public long getTimeElapsed() {
		if (this.timer != null) {
			return this.timer.getTimeElapsed();
		}
		return -1;
	}
	
	protected class TimeoutTimer implements Runnable {
		
		private final LocalDateTime startTime;
		private final long timeUnitAmount;
		private final TemporalUnit timeUnit;
		private final HasTimeout notifyTarget;
		
		private volatile boolean isReset = false;
		private volatile boolean isRunning = true;
		
		protected TimeoutTimer(LocalDateTime startTime, long timeUnitAmount, TemporalUnit timeUnit, HasTimeout notifyTarget) {
			this.startTime = startTime;
			this.timeUnitAmount = timeUnitAmount;
			this.timeUnit = timeUnit;
			this.notifyTarget = notifyTarget;
		}
		
		protected long getTimeElapsed() {
			return this.getStartTime().until(LocalDateTime.now(), this.getTimeUnit());
		}
		
		protected boolean isTimeUp() {
			return this.getTimeUnitAmount() <= this.getTimeElapsed();
		}
		
		protected void reset() {
			this.isReset = true;
//			System.out.println(this + " reset() in " + this.getNotifyTarget());
		}
		
		protected HasTimeout getNotifyTarget() {
			return this.notifyTarget;
		}
		
		protected void notifyTarget() {
			if (this.getNotifyTarget() != null) {
				this.getNotifyTarget().timeout();
			}
		}
		
		protected void notifyStop() {
			if (this.getNotifyTarget() != null) {
				this.getNotifyTarget().timeoutTimerStopped(this.isReset(), !this.isRunning());
			}
		}
		
		protected boolean isReset() {
			return this.isReset;
		}
		
		protected TemporalUnit getTimeUnit() {
			return this.timeUnit;
		}
		
		protected long getTimeUnitAmount() {
			return this.timeUnitAmount;
		}
		
		protected LocalDateTime getStartTime() {
			return this.startTime;
		}

		protected boolean isRunning() {
			return this.isRunning;
		}
		
		protected void waitTillTimeIsUp() {
			while (!this.isTimeUp() && this.isRunning() && !this.isReset()) {
//				if (this.isReset()) {
//					System.out.println(this + " Timer reset by notify target: " + this.getNotifyTarget());
//					break;
//				}
			}
		}
		
		protected void notifyAlgorithm() {
			if (!this.isReset() && this.isRunning()) {
//				System.out.println(this + " Notifying target: " + this.getNotifyTarget());
				this.notifyTarget();
			}
		}
		
		protected void endAlgorithm() {
//			timer = null;
			this.notifyStop();
			this.isRunning = false;
		}
		
		@Override
		public void run() {
//			System.out.println(this + " Timer started: " + this.getNotifyTarget());
			this.waitTillTimeIsUp();
//			System.out.println(this + " TimeUp/Reset: " + this.getNotifyTarget());
			this.notifyAlgorithm();
//			System.out.println(this + " Cycle over: " + this.getNotifyTarget());
			this.endAlgorithm();
		}
	}
}