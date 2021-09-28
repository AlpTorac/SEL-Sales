package test.external.buffer;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.buffer.ISendBuffer;
import external.buffer.StandardSendBuffer;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageFormat;
import external.message.StandardMessageFormat;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnection;
@Execution(value = ExecutionMode.SAME_THREAD)
class StandardSendBufferTest {
	private ISendBuffer sb;
	private ExecutorService es;
	private DummyConnection conn;
	private MessageFormat messageFormat = new StandardMessageFormat();
	private String fieldSeparator;
	private String fieldElementSeparator;
	
	@BeforeEach
	void prep() {
		conn = new DummyConnection("clientaddress");
		es = Executors.newFixedThreadPool(3);
		sb = new StandardSendBuffer(conn, es);
		this.fieldSeparator = this.messageFormat.getDataFieldSeparatorForString();
		this.fieldElementSeparator = this.messageFormat.getDataFieldElementSeparatorForString();
	}
	@AfterEach
	void cleanUp() {
		try {
			sb.close();
			conn.close();
			es.shutdown();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * Packed in one test case to prevent JUnit parallelisation to
	 * mess things up.
	 */
//	@Test
//	void mainBufferTest() {
//		this.prep();
//		this.addMessageTest();
//		this.cleanUp();
//		
//		this.prep();
//		this.sendAndReceiveAcknowledgementTest();
//		this.cleanUp();
//		
//		this.prep();
//		this.sendAndReceiveLateAcknowledgementTest();
//		this.cleanUp();
//		
//		this.prep();
//		this.timeoutTest();
//		this.cleanUp();
//		
//		this.prep();
//		this.repetitiveSendTest();
//		this.cleanUp();
//		
//		this.prep();
//		this.emptySendTest();
//		this.cleanUp();
//		
//		this.prep();
//		this.receiveAckFailTest();
//		this.cleanUp();
//	}
	@Test
	void addMessageTest() {
		Assertions.assertTrue(sb.isEmpty());
		String serialisedData = "abcdefg";
		IMessage message = new Message(null, null, serialisedData);
		sb.addMessage(message);
		Assertions.assertFalse(sb.isEmpty());
	}
	@Test
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
	@Test
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
	@Test
	void timeoutTest() {
		String sd1 = "abcdefg";
		IMessage m1 = new Message(null, null, sd1);
		String sentMessage = messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				m1.getSerialisedData() + messageFormat.getMessageEnd();
		Assertions.assertTrue(sb.isEmpty());
		ByteArrayOutputStream os = conn.getOutputStream();
		BufferUtilityClass.assertMessageSent(sb, 0, m1);
		Assertions.assertFalse(sb.isEmpty());
		
		BufferUtilityClass.assertOutputWrittenEquals(os, sentMessage.getBytes());
		
		long waitDuration = 2100;
		long remainingWait = 600;
		GeneralTestUtilityClass.performWait(waitDuration - remainingWait);
//		os = conn.getOutputStream();
		GeneralTestUtilityClass.performWait(remainingWait);
		
		Assertions.assertTrue(sb.isBlocked());
		Assertions.assertFalse(sb.isEmpty());
		BufferUtilityClass.assertAcknowledgementOfMessageReceived(sb, m1);
		Assertions.assertTrue(sb.isEmpty());
		
//		BufferUtilityClass.assertOutputWrittenEquals(os, sentMessage.getBytes());
	}
	@Test
	void repetitiveSendTest() {
		String sd1 = "abcdefg";
		IMessage m1 = new Message(null, null, sd1);
		BufferUtilityClass.assertMessageSent(sb, 0, m1);
		Assertions.assertFalse(sb.sendMessage());
		BufferUtilityClass.assertAcknowledgementOfMessageReceived(sb, m1);
	}
	@Test
	void emptySendTest() {
		Assertions.assertFalse(sb.sendMessage());
	}
	@Test
	void receiveAckFailTest() {
		IMessage m = new Message(null, null, null);
		sb.addMessage(m);
		Assertions.assertTrue(sb.sendMessage());
		
		IMessage nonAck = new Message(null, null, null);
		sb.receiveAcknowledgement(nonAck);
		Assertions.assertTrue(sb.isBlocked());
	}
}
