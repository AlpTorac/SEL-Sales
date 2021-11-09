package test.external.buffer;

import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;

import external.connection.outgoing.ISendBuffer;
import external.message.IMessage;

public final class BufferUtilityClass {
//	public static void assertOutputWrittenEquals(ByteArrayOutputStream os, byte[] written) {
//		Assertions.assertArrayEquals(written, os.toByteArray());
//	}
//	public static void assertOutputWrittenContains(ByteArrayOutputStream os, byte[] written) {
//		Assertions.assertTrue(os.toString().contains(new String(written)));
//	}
	public static void assertInputStoredEquals(InputStream is, byte[] read) {
		String sBuffer = null;
		String readString = new String(read);
		try {
			sBuffer = new String(is.readAllBytes());
			Assertions.assertEquals(readString, sBuffer);
		} catch (IOException e) {
			e.printStackTrace();
			Assertions.fail("Input stream content: " + sBuffer + "\n read: " + readString);
		}
	}
	public static void assertMessageSent(ISendBuffer sb, int sequenceNumber, IMessage message) {
		sb.addMessage(message);
		Assertions.assertTrue(sb.sendMessage());
		Assertions.assertEquals(sequenceNumber, message.getSequenceNumber());
		Assertions.assertTrue(sb.isBlocked());
	}
	public static void assertAcknowledgementOfMessageReceived(ISendBuffer sb, IMessage message) {
		assertAcknowledgementReceived(sb, message.getMinimalAcknowledgementMessage());
	}
	public static void assertAcknowledgementReceived(ISendBuffer sb, IMessage ackMessage) {
		sb.receiveAcknowledgement(ackMessage);
		while (sb.hasRunningTimer()) {
			
		}
		Assertions.assertFalse(sb.isBlocked());
	}
	
	public static void assertMessageAcknowledgementCycleSuccessful(ISendBuffer sb, int sequenceNumber, IMessage message) {
		assertMessageSent(sb, sequenceNumber, message);
		assertAcknowledgementOfMessageReceived(sb, message);
	}
}
