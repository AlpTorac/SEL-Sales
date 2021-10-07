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
		if (this.timer == null && !this.es.isShutdown()) {
			this.es.submit((this.timer = new TimeoutTimer(LocalDateTime.now(), this.getTimeUnitAmount(), this.getTimeUnit(), this.notifyTarget)));
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
	@Override
	public boolean isRunning() {
		return this.timer != null;
	}
	
	protected class TimeoutTimer implements Runnable {
		
		private LocalDateTime startTime;
		private long timeUnitAmount;
		private TemporalUnit timeUnit;
		private HasTimeout notifyTarget;
		
		private boolean isReset = false;
		
		protected TimeoutTimer(LocalDateTime startTime, long timeUnitAmount, TemporalUnit timeUnit, HasTimeout notifyTarget) {
			this.startTime = startTime;
			this.timeUnitAmount = timeUnitAmount;
			this.timeUnit = timeUnit;
			this.notifyTarget = notifyTarget;
		}
		
		protected boolean isTimeUp() {
			return this.getTimeUnitAmount() <= this.getStartTime().until(LocalDateTime.now(), this.getTimeUnit());
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

		@Override
		public void run() {
//			System.out.println(this + " Timer started: " + this.getNotifyTarget());
			
			while (!this.isTimeUp()) {
				if (this.isReset()) {
//					System.out.println(this + " Timer reset by notify target: " + this.getNotifyTarget());
					break;
				}
			}
			
//			System.out.println(this + " TimeUp/Reset: " + this.getNotifyTarget());
			
			if (!this.isReset()) {
//				System.out.println(this + " Notifying target: " + this.getNotifyTarget());
				this.notifyTarget();
			}
//			System.out.println(this + " Cycle over: " + this.getNotifyTarget());
			timer = null;
		}
	}
}