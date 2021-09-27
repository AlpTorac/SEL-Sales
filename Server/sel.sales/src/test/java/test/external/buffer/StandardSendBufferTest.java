package test.external.buffer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import external.buffer.ISendBuffer;
import external.buffer.StandardSendBuffer;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageFormat;
import external.message.StandardMessageFormat;
import test.GeneralTestUtilityClass;

class StandardSendBufferTest {
	private ISendBuffer sb;
	private ExecutorService es;
	private ByteArrayOutputStream os;
	private MessageFormat messageFormat = new StandardMessageFormat();
	private String fieldSeparator;
	private String fieldElementSeparator;
	
	void prep() {
		os = new ByteArrayOutputStream();
		es = Executors.newFixedThreadPool(3);
		sb = new StandardSendBuffer(os, es);
		this.fieldSeparator = this.messageFormat.getDataFieldSeparatorForString();
		this.fieldElementSeparator = this.messageFormat.getDataFieldElementSeparatorForString();
	}
	
	void cleanUp() {
		try {
			sb.close();
			os.close();
			es.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Packed in one test case to prevent JUnit parallelisation to
	 * mess things up.
	 */
	@Test
	void mainBufferTest() {
		this.prep();
		this.addMessageTest();
		this.cleanUp();
		
		this.prep();
		this.sendAndReceiveAcknowledgementTest();
		this.cleanUp();
		
		this.prep();
		this.sendAndReceiveLateAcknowledgementTest();
		this.cleanUp();
		
		this.prep();
		this.timeoutTest();
		this.cleanUp();
		
		this.prep();
		this.repetitiveSendTest();
		this.cleanUp();
		
		this.prep();
		this.emptySendTest();
		this.cleanUp();
		
		this.prep();
		this.receiveAckFailTest();
		this.cleanUp();
	}
	
	void addMessageTest() {
		Assertions.assertTrue(sb.isEmpty());
		String serialisedData = "abcdefg";
		IMessage message = new Message(null, null, serialisedData);
		sb.addMessage(message);
		Assertions.assertFalse(sb.isEmpty());
	}
	
	void sendAndReceiveAcknowledgementTest() {
		Assertions.assertTrue(sb.isEmpty());
		String sd1 = "abcdefg";
		IMessage m1 = new Message(null, null, sd1);
		BufferUtilityClass.assertMessageAcknowledgementCycleSuccessful(sb, 0, m1);
		
		Assertions.assertTrue(sb.isEmpty());
		
		String sd2 = "hjhgdfhkgf";
		IMessage m2 = new Message(null, null, sd2);
		BufferUtilityClass.assertMessageAcknowledgementCycleSuccessful(sb, 1, m2);
		Assertions.assertTrue(sb.isEmpty());
	}
	
	void sendAndReceiveLateAcknowledgementTest() {
		String sd1 = "abcdefg";
		IMessage m1 = new Message(null, null, sd1);
		Assertions.assertTrue(sb.isEmpty());
		BufferUtilityClass.assertMessageSent(sb, 0, m1);
		Assertions.assertFalse(sb.isEmpty());
		
		long waitDuration = 500;
		GeneralTestUtilityClass.performWait(waitDuration);
		
		Assertions.assertFalse(sb.isEmpty());
		BufferUtilityClass.assertAcknowledgementOfMessageReceived(sb, m1);
		Assertions.assertTrue(sb.isEmpty());
	}
	
	void timeoutTest() {
		String sd1 = "abcdefg";
		IMessage m1 = new Message(null, null, sd1);
		String sentMessage = messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				m1.getSerialisedData() + messageFormat.getMessageEnd();
		Assertions.assertTrue(sb.isEmpty());
		BufferUtilityClass.assertMessageSent(sb, 0, m1);
		Assertions.assertFalse(sb.isEmpty());
		
		BufferUtilityClass.assertOutputWrittenEquals(os, sentMessage.getBytes());
		
		long waitDuration = 2100;
		GeneralTestUtilityClass.performWait(waitDuration);
		
		Assertions.assertFalse(sb.isEmpty());
		BufferUtilityClass.assertAcknowledgementOfMessageReceived(sb, m1);
		Assertions.assertTrue(sb.isEmpty());
		
		BufferUtilityClass.assertOutputWrittenEquals(os, (sentMessage+sentMessage).getBytes());
	}
	
	void repetitiveSendTest() {
		String sd1 = "abcdefg";
		IMessage m1 = new Message(null, null, sd1);
		BufferUtilityClass.assertMessageSent(sb, 0, m1);
		Assertions.assertFalse(sb.sendMessage());
		BufferUtilityClass.assertAcknowledgementOfMessageReceived(sb, m1);
	}
	
	void emptySendTest() {
		Assertions.assertFalse(sb.sendMessage());
	}
	
	void receiveAckFailTest() {
		IMessage m = new Message(null, null, null);
		sb.addMessage(m);
		Assertions.assertTrue(sb.sendMessage());
		
		IMessage nonAck = new Message(null, null, null);
		sb.receiveAcknowledgement(nonAck);
		Assertions.assertTrue(sb.isBlocked());
	}
}
