package test.external.buffer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.Assertions;

import external.connection.outgoing.ISendBuffer;
import external.message.IMessage;

public final class BufferUtilityClass {
	public static void assertOutputWrittenEquals(ByteArrayOutputStream os, byte[] written) {
		Assertions.assertArrayEquals(written, os.toByteArray());
	}
	public static void assertOutputWrittenContains(ByteArrayOutputStream os, byte[] written) {
		Assertions.assertTrue(os.toString().contains(new String(written)));
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
	public static void assertAcknowledgementOfMessageReceived(ISendBuffer sb, IMessage message) {
		assertAcknowledgementReceived(sb, message.getMinimalAcknowledgementMessage());
	}
	public static void assertAcknowledgementReceived(ISendBuffer sb, IMessage ackMessage) {
		sb.receiveAcknowledgement(ackMessage);
		Assertions.assertFalse(sb.isBlocked());
	}
	
	public static void assertMessageAcknowledgementCycleSuccessful(ISendBuffer sb, int sequenceNumber, IMessage message) {
		assertMessageSent(sb, sequenceNumber, message);
		assertAcknowledgementOfMessageReceived(sb, message);
	}
	
	public static void fillBuffer(byte[] isBuffer, String bufferContent) {
		byte[] bytes = bufferContent.getBytes();
		int i = 0;
		byte currentByte = isBuffer[i];
		while (currentByte != 0) {
			i++;
			currentByte = isBuffer[i];
		}
		for (int j = 0; j < bytes.length; j++) {
			isBuffer[i+j] = bytes[j];
		}
	}
}
