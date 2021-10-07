package test.external.timeout;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.connection.timeout.ITimeoutStrategy;
import external.connection.timeout.PingPongTimeoutStrategy;

class PingPongTimeoutStrategyTest {
	private long testCompensationInMillis = 100;
	private long toleranceInMillis;
	private ExecutorService es;
	private long esTerminationTimeout;
	private LocalDateTime startTime;
	private long timeoutInMillis;
	private long shortestRandomDuration;
	private long longestRandomDuration;
	private ITimeoutStrategy ts;
	
	private long minimalDelay;
	
	@BeforeEach
	void prep() {
		minimalDelay = 0;
		toleranceInMillis = 100;
		esTerminationTimeout = 100;
		es = Executors.newCachedThreadPool();
		timeoutInMillis = 500;
		shortestRandomDuration = timeoutInMillis / 10;
		longestRandomDuration = timeoutInMillis;
	}
	
	private void initTimeoutStrategy() {
		startTime = LocalDateTime.now();
		ts = new PingPongTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS, es, minimalDelay);
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
		System.out.println("Took " + startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) + " ms to terminate - Ping Pong timeout strategy");
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
		TimeoutTestUtilityClass.assertTimeoutShorter(startTime, 0, toleranceInMillis + testCompensationInMillis);
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
	void minimalWaitTest() {
		timeoutInMillis = 100;
		minimalDelay = 300;
		
		shortestRandomDuration = timeoutInMillis / 10;
		longestRandomDuration = timeoutInMillis;
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, minimalDelay+timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, minimalDelay+timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
		this.initTimeoutStrategy();
		TimeoutTestUtilityClass.assertRandomTimeoutResetSuccessful(ts, startTime, minimalDelay+timeoutInMillis, toleranceInMillis, shortestRandomDuration, longestRandomDuration);
	}
}
