package test.external.buffer;

import static org.junit.jupiter.api.Assertions.*;

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
import external.connection.timeout.HasTimeout;
import external.connection.timeout.ITimeoutStrategy;
import test.GeneralTestUtilityClass;
@Execution(value = ExecutionMode.SAME_THREAD)
class FixTimeoutStrategyTest {
	private long testCompensationInMillis = 300;
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
		toleranceInMillis = 100;
		esTerminationTimeout = 100;
		es = Executors.newFixedThreadPool(10);
		timeoutInMillis = 500;
		shortestRandomDuration = timeoutInMillis / 10;
		longestRandomDuration = timeoutInMillis;
	}
	
	/**
	 * Not in prep() to allow last minute custom settings changes.
	 */
	private void initTimeoutStrategy() {
		startTime = LocalDateTime.now();
		ts = new FixTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS);
		ts.startTimer();
		es.submit(ts);
	}
	
	@AfterEach
	void cleanUp() {
		ts.terminate();
		try {
			es.awaitTermination(esTerminationTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void noResetTest() {
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.failIfToleranceViolated(ts, startTime, timeoutInMillis, toleranceInMillis);
		TimeoutTestUtilityClass.assertTimeoutLonger(startTime, timeoutInMillis, toleranceInMillis);
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
		TimeoutTestUtilityClass.assertTimeoutShorter(startTime, 0, toleranceInMillis + testCompensationInMillis);
	}
	
	@Test
	void multipleUseTest() {
		timeoutInMillis = 100;
		shortestRandomDuration = timeoutInMillis / 10;
		longestRandomDuration = timeoutInMillis;
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
	}
}
