package test.external.connection;

import static org.junit.jupiter.api.Assertions.fail;

import java.io.ByteArrayInputStream;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import external.connection.outgoing.ISendBuffer;
import test.GeneralTestUtilityClass;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyConnection;

public final class ConnectionManagerTestUtilityClass {
	public static void assertAckReadAndSentToSendBuffer(DummyConnection conn, ISendBuffer sb, String serialisedIncAck, long readWaitTime, long totalWaitTime) {
		LocalDateTime now = LocalDateTime.now();
		ByteArrayInputStream is = conn.getInputStream();
		while (sb.isBlocked()) {
			is = conn.getInputStream();
			is.reset();
			BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialisedIncAck);
			GeneralTestUtilityClass.performWait(readWaitTime);
			if (now.until(LocalDateTime.now(), ChronoUnit.MILLIS) > totalWaitTime) {
				fail("Send buffer took too long to get unblocked");
			}
		}
	}
}
