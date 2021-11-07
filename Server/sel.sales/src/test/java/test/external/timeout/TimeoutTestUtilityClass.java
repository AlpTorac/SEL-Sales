package test.external.timeout;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;

import org.junit.jupiter.api.Assertions;

import external.connection.timeout.ITimeoutStrategy;
import test.GeneralTestUtilityClass;

public final class TimeoutTestUtilityClass {
	public static void failIfToleranceViolated(ITimeoutStrategy ts, LocalDateTime startTime, long durationInMillis, long toleranceInMillis, TemporalUnit unit) {
		while (ts.hasRunningTimer()) {
			if (startTime.until(LocalDateTime.now(), unit) > durationInMillis + toleranceInMillis) {
				fail("Timer exceeds the tolerated delay.");
			}
		}
	}
	
	public static void failIfToleranceViolated(ITimeoutStrategy ts, LocalDateTime startTime, long timeoutInMillis, long toleranceInMillis) {
		while (ts.hasRunningTimer()) {
			if (startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) > timeoutInMillis + toleranceInMillis) {
				fail("Timer exceeds the tolerated delay.");
			}
		}
	}
	
	public static void assertTimeoutCorrect(LocalDateTime startTime, long durationInMillis, long toleranceInMillis, TemporalUnit unit) {
		assertTimeoutShorter(startTime, durationInMillis, toleranceInMillis, unit);
		assertTimeoutLonger(startTime, durationInMillis, toleranceInMillis, unit);
	}
	
	public static void assertTimeoutCorrect(LocalDateTime startTime, long durationInMillis, long toleranceInMillis) {
		assertTimeoutShorter(startTime, durationInMillis, toleranceInMillis);
		assertTimeoutLonger(startTime, durationInMillis, toleranceInMillis);
	}
	
	public static void assertTimeoutLonger(LocalDateTime startTime, long duration, long tolerance, TemporalUnit unit) {
		Assertions.assertTrue(startTime.until(LocalDateTime.now(), unit) >= duration - tolerance);
	}
	
	public static void assertTimeoutShorter(LocalDateTime startTime, long duration, long tolerance, TemporalUnit unit) {
		Assertions.assertTrue(startTime.until(LocalDateTime.now(), unit) <= duration + tolerance);
	}
	
	public static void assertTimeoutLonger(LocalDateTime startTime, long durationInMillis, long toleranceInMillis) {
		assertTimeoutLonger(startTime, durationInMillis, toleranceInMillis, ChronoUnit.MILLIS);
	}
	
	public static void assertTimeoutShorter(LocalDateTime startTime, long durationInMillis, long toleranceInMillis) {
		assertTimeoutShorter(startTime, durationInMillis, toleranceInMillis, ChronoUnit.MILLIS);
	}
	
	public static void assertTimeoutResetSuccessful(ITimeoutStrategy ts, long waitDuration, LocalDateTime startTime, long timeoutInMillis, long toleranceInMillis) {
		GeneralTestUtilityClass.performWait(waitDuration);
		ts.reset();
		failIfToleranceViolated(ts, startTime, timeoutInMillis, toleranceInMillis);
	}
	
	public static void assertRandomTimeoutResetSuccessful(ITimeoutStrategy ts, LocalDateTime startTime,  long timeoutInMillis, long toleranceInMillis, long shortestRandomDuration, long longestRandomDuration) {
		long waitTime = GeneralTestUtilityClass.generateRandomNumber(shortestRandomDuration, longestRandomDuration);
		TimeoutTestUtilityClass.assertTimeoutResetSuccessful(ts, waitTime, startTime, timeoutInMillis, toleranceInMillis);
	}
}
