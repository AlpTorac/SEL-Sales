package test.external.buffer;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.buffer.FixTimeoutStrategy;
import external.buffer.HasTimeout;
import external.buffer.ITimeoutStrategy;

class FixTimeoutStrategyTest {
	private long toleranceInMillis = 50;
	private ExecutorService es;
	private long esTerminationTimeout = 50;
	
	@BeforeEach
	void prep() {
		es = Executors.newFixedThreadPool(2);
	}
	
	@Test
	void noResetTest() {
		LocalDateTime startTime = LocalDateTime.now();
		long timeoutInMillis = 500;
		ITimeoutStrategy ts = new FixTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS);
		ts.startTimer();
		es.submit(ts);
		while (ts.isRunning()) {
			if (startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) > timeoutInMillis + toleranceInMillis) {
				fail("Timer exceeds the tolerated delay.");
			}
		}
		Assertions.assertTrue(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) >= timeoutInMillis);
		ts.terminate();
		try {
			es.awaitTermination(esTerminationTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void resetWithWorkTimeTest() {
		LocalDateTime startTime = LocalDateTime.now();
		long timeoutInMillis = 500;
		ITimeoutStrategy ts = new FixTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS);
		ts.startTimer();
		es.submit(ts);
		long waitTime = 200;
		try {
			Thread.sleep(waitTime);
		} catch (InterruptedException e1) {
			e1.printStackTrace();
		}
		ts.reset();
		while (ts.isRunning()) {
			if (startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) > timeoutInMillis + toleranceInMillis) {
				fail("Timer exceeds the tolerated delay.");
			}
		}
		Assertions.assertTrue(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) >= waitTime);
		Assertions.assertTrue(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) < waitTime + toleranceInMillis);
		ts.terminate();
		try {
			es.awaitTermination(esTerminationTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	@Test
	void immediateResetTest() {
		LocalDateTime startTime = LocalDateTime.now();
		long timeoutInMillis = 500;
		ITimeoutStrategy ts = new FixTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS);
		ts.startTimer();
		es.submit(ts);
		ts.reset();
		while (ts.isRunning()) {
			if (startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) > toleranceInMillis) {
				fail("Timer exceeds the tolerated delay.");
			}
		}
		Assertions.assertTrue(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) < toleranceInMillis);
		ts.terminate();
		try {
			es.awaitTermination(esTerminationTimeout, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
}
