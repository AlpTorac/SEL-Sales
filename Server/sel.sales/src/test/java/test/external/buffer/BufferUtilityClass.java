package test.external.buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;

import external.buffer.ISendBuffer;
import external.message.IMessage;

public final class BufferUtilityClass {
	public static void assertOutputWrittenEquals(ByteArrayOutputStream os, byte[] written) {
		Assertions.assertArrayEquals(written, os.toByteArray());
	}
	public static void assertInputStoredEquals(InputStream is, byte[] read) {
		try {
			Assertions.assertArrayEquals(read, is.readAllBytes());
		} catch (IOException e) {
			e.printStackTrace();
			Assertions.fail();
		}
	}
	public static void assertMessageSent(ISendBuffer sb, int sequenceNumber, IMessage message) {
		sb.addMessage(message);
		Assertions.assertTrue(sb.sendMessage());
		Assertions.assertEquals(sequenceNumber, message.getSequenceNumber());
		Assertions.assertTrue(sb.isBlocked());
	}
	public static void assertAcknowledgementReceived(ISendBuffer sb, IMessage message) {
		sb.receiveAcknowledgement(message.getMinimalAcknowledgementMessage());
		Assertions.assertFalse(sb.isBlocked());
	}
	
	public static void assertMessageAcknowledgementCycleSuccessful(ISendBuffer sb, int sequenceNumber, IMessage message) {
		assertMessageSent(sb, sequenceNumber, message);
		assertAcknowledgementReceived(sb, message);
	}
}
