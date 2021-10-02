package test.external.buffer;

import static org.junit.jupiter.api.Assertions.fail;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import org.junit.jupiter.api.Assertions;

import external.connection.timeout.ITimeoutStrategy;
import test.GeneralTestUtilityClass;

public final class TimeoutTestUtilityClass {
	public static void failIfToleranceViolated(ITimeoutStrategy ts, LocalDateTime startTime, long timeoutInMillis, long toleranceInMillis) {
		while (ts.isRunning()) {
			if (startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) > timeoutInMillis + toleranceInMillis) {
				fail("Timer exceeds the tolerated delay.");
			}
		}
	}
	
	public static void assertTimeoutCorrect(LocalDateTime startTime, long durationInMillis, long toleranceInMillis) {
		assertTimeoutLonger(startTime, durationInMillis, toleranceInMillis);
		assertTimeoutShorter(startTime, durationInMillis, toleranceInMillis);
	}
	
	public static void assertTimeoutLonger(LocalDateTime startTime, long durationInMillis, long toleranceInMillis) {
		Assertions.assertTrue(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) > durationInMillis - toleranceInMillis);
	}
	
	public static void assertTimeoutShorter(LocalDateTime startTime, long durationInMillis, long toleranceInMillis) {
		Assertions.assertTrue(startTime.until(LocalDateTime.now(), ChronoUnit.MILLIS) < durationInMillis + toleranceInMillis);
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
