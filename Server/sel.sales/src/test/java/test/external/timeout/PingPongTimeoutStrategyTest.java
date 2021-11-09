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
	private PingPongTimeoutStrategy ts;
	
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
	
	@Test
	void timeUnitChangedTest() {
		this.initTimeoutStrategy();
		long waitTime = 200;
		TimeoutTestUtilityClass.assertTimeoutResetSuccessful(ts, waitTime, startTime, timeoutInMillis, toleranceInMillis);
		timeoutInMillis = 1;
		ts = new PingPongTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS, es, minimalDelay);
		this.ts.setTimeUnit(ChronoUnit.SECONDS);
		Assertions.assertEquals(ChronoUnit.SECONDS, this.ts.getTimeUnit());
		Assertions.assertEquals(timeoutInMillis, this.ts.getTimeUnitAmount());
		startTime = LocalDateTime.now();
		this.ts.startTimer();
		TimeoutTestUtilityClass.assertTimeoutCorrect(startTime, timeoutInMillis, toleranceInMillis);
//		TimeoutTestUtilityClass.failIfToleranceViolated(ts, LocalDateTime.now(), timeoutInMillis, toleranceInMillis, ChronoUnit.SECONDS);
	}
	
	@Test
	void timeUnitAmountChangedTest() {
		this.initTimeoutStrategy();
		long waitTime = 200;
		TimeoutTestUtilityClass.assertTimeoutResetSuccessful(ts, waitTime, startTime, timeoutInMillis, toleranceInMillis);
		ts = new PingPongTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS, es, minimalDelay);
		timeoutInMillis = 0;
		ts.setTimeUnitAmount(timeoutInMillis);
		Assertions.assertEquals(ChronoUnit.MILLIS, this.ts.getTimeUnit());
		Assertions.assertEquals(timeoutInMillis, this.ts.getTimeUnitAmount());
		startTime = LocalDateTime.now();
		this.ts.startTimer();
		TimeoutTestUtilityClass.failIfToleranceViolated(ts, startTime, timeoutInMillis, toleranceInMillis);
//		TimeoutTestUtilityClass.assertTimeoutCorrect(startTime, timeoutInMillis, toleranceInMillis);
		
//		TimeoutTestUtilityClass.failIfToleranceViolated(ts, LocalDateTime.now(), timeoutInMillis, toleranceInMillis, ChronoUnit.SECONDS);
	}
	
	@Test
	void minimalDelayChangedTest() {
		this.initTimeoutStrategy();
		long waitTime = 200;
		TimeoutTestUtilityClass.assertTimeoutResetSuccessful(ts, waitTime, startTime, timeoutInMillis, toleranceInMillis);
		timeoutInMillis = 500;
		ts = new PingPongTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS, es, minimalDelay);
		minimalDelay = 500;
		ts.setMinimalDelay(minimalDelay);
		Assertions.assertEquals(ChronoUnit.MILLIS, this.ts.getTimeUnit());
		Assertions.assertEquals(minimalDelay, this.ts.getMinimalDelay());
		startTime = LocalDateTime.now();
		this.ts.startTimer();
		TimeoutTestUtilityClass.failIfToleranceViolated(ts, startTime, minimalDelay, toleranceInMillis);
//		TimeoutTestUtilityClass.assertTimeoutCorrect(startTime, minimalDelay, toleranceInMillis);
	}
}
