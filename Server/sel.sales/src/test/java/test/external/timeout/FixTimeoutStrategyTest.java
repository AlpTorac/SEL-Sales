package test.external.timeout;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.connection.timeout.FixTimeoutStrategy;
import external.connection.timeout.ITimeoutStrategy;
@Execution(value = ExecutionMode.SAME_THREAD)
class FixTimeoutStrategyTest {
	private long testCompensationInMillis = 100;
	private long toleranceInMillis;
	private ExecutorService es;
	private long esTerminationTimeout;
	private LocalDateTime startTime;
	private long timeoutInMillis;
	private long shortestRandomDuration;
	private long longestRandomDuration;
	private ITimeoutStrategy ts;
	
	@BeforeEach
	void prep() {
		toleranceInMillis = 50;
		esTerminationTimeout = 100;
		es = Executors.newCachedThreadPool();
		timeoutInMillis = 500;
		shortestRandomDuration = timeoutInMillis / 10;
		longestRandomDuration = timeoutInMillis;
	}
	
	/**
	 * Not in prep() to allow last minute custom settings changes.
	 */
	private void initTimeoutStrategy() {
		startTime = LocalDateTime.now();
		ts = new FixTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS, es);
		ts.startTimer();
	}
	
	@AfterEach
	void cleanUp() {
		try {
			ts.reset();
			es.awaitTermination(esTerminationTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void noResetTest() {
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.failIfToleranceViolated(ts, startTime, timeoutInMillis, toleranceInMillis);
		System.out.println("Took " + startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) + " ms to terminate - Fix timeout strategy");
		TimeoutTestUtilityClass.assertTimeoutCorrect(startTime, timeoutInMillis, toleranceInMillis);
	}
	
	@Test
	void resetWithWorkTimeTest() {
		this.initTimeoutStrategy();
		long waitTime = 200;
		TimeoutTestUtilityClass.assertTimeoutResetSuccessful(ts, waitTime, startTime, timeoutInMillis, toleranceInMillis);
	}

	@Test
	void immediateResetTest() {
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.assertTimeoutResetSuccessful(ts, 0, startTime, 0, toleranceInMillis);
		TimeoutTestUtilityClass.assertTimeoutShorter(startTime, 0, toleranceInMillis);
	}
	
	@Test
	void multipleUseTest() {
		timeoutInMillis = 100;
		shortestRandomDuration = timeoutInMillis / 10;
		longestRandomDuration = timeoutInMillis;
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
	}
	
	@Test
	void timeUnitChangedTest() {
		this.initTimeoutStrategy();
		long waitTime = 200;
		TimeoutTestUtilityClass.assertTimeoutResetSuccessful(ts, waitTime, startTime, timeoutInMillis, toleranceInMillis);
		timeoutInMillis = 1;
		ts = new FixTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS, es);
		this.ts.setTimeUnit(ChronoUnit.SECONDS);
		Assertions.assertEquals(ChronoUnit.SECONDS, this.ts.getTimeUnit());
		Assertions.assertEquals(timeoutInMillis, this.ts.getTimeUnitAmount());
		startTime = LocalDateTime.now();
		this.ts.startTimer();
//		TimeoutTestUtilityClass.assertTimeoutCorrect(startTime, timeoutInMillis, toleranceInMillis);
		TimeoutTestUtilityClass.failIfToleranceViolated(ts, startTime, timeoutInMillis, toleranceInMillis, ChronoUnit.SECONDS);
	}
	
	@Test
	void timeUnitAmountChangedTest() {
		this.initTimeoutStrategy();
		long waitTime = 200;
		TimeoutTestUtilityClass.assertTimeoutResetSuccessful(ts, waitTime, startTime, timeoutInMillis, toleranceInMillis);
		ts = new FixTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS, es);
		timeoutInMillis = 0;
		ts.setTimeUnitAmount(timeoutInMillis);
		Assertions.assertEquals(ChronoUnit.MILLIS, this.ts.getTimeUnit());
		Assertions.assertEquals(timeoutInMillis, this.ts.getTimeUnitAmount());
		startTime = LocalDateTime.now();
		this.ts.startTimer();
//		TimeoutTestUtilityClass.assertTimeoutCorrect(LocalDateTime.now(), timeoutInMillis, toleranceInMillis);
		TimeoutTestUtilityClass.failIfToleranceViolated(ts, startTime, timeoutInMillis, toleranceInMillis, ChronoUnit.SECONDS);
	}
}
