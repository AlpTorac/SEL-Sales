package test.external.buffer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.connection.outgoing.ISendBuffer;
import external.connection.outgoing.StandardSendBuffer;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageFormat;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnection;
@Execution(value = ExecutionMode.SAME_THREAD)
class StandardSendBufferTest {
	private long timeout = 200;
	private ISendBuffer sb;
	private ExecutorService es;
	private DummyConnection senderConn;
	private DummyConnection receiverConn;
	private MessageFormat messageFormat = new StandardMessageFormat();
	private MessageSerialiser serialiser = new MessageSerialiser(messageFormat);
	private String fieldSeparator;
	private String fieldElementSeparator;
	
	@BeforeEach
	void prep() {
		senderConn = new DummyConnection("DeviceAddress");
		receiverConn = new DummyConnection("receiverAddress");
		senderConn.setInputTarget(receiverConn.getInputStream());
		es = Executors.newCachedThreadPool();
		sb = new StandardSendBuffer(senderConn, es, timeout);
		this.fieldSeparator = this.messageFormat.getDataFieldSeparatorForString();
		this.fieldElementSeparator = this.messageFormat.getDataFieldElementSeparatorForString();
	}
	@AfterEach
	void cleanUp() {
		try {
			sb.close();
			receiverConn.close();
			senderConn.close();
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
		
		while (sb.hasRunningTimer()) {
			
		}
		
		String sd2 = "hjhgdfhkgf";
		IMessage m2 = new Message(1, null, null, sd2);
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
		
		long waitDuration = timeout / 4;
		GeneralTestUtilityClass.performWait(waitDuration);
		
		Assertions.assertFalse(sb.isEmpty());
		BufferUtilityClass.assertAcknowledgementOfMessageReceived(sb, m1);
		Assertions.assertTrue(sb.isEmpty());
	}
	@Test
	void timeoutTest() {
		String sd1 = "abcdefg";
		IMessage m1 = new Message(null, null, sd1);
		String sentMessage = serialiser.serialise(m1);
		Assertions.assertTrue(sb.isEmpty());
		BufferUtilityClass.assertMessageSent(sb, 0, m1);
		Assertions.assertFalse(sb.isEmpty());
		
		BufferUtilityClass.assertInputStoredEquals(this.receiverConn.getInputStream(), sentMessage.getBytes());
		
		long waitDuration = timeout + timeout / 4;
//		long remainingWait = 600;
		GeneralTestUtilityClass.performWait(waitDuration);
//		GeneralTestUtilityClass.performWait(waitDuration - remainingWait);
//		os = conn.getOutputStream();
//		GeneralTestUtilityClass.performWait(remainingWait);
		
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
	@Test
	void recoveryTest() {
		long waitDuration = timeout / 4;
		long timeoutDuration = timeout + timeout / 4;
		int sequenceNumber = 0;
		int successfulCyclesToWait = 20;
		ArrayList<Boolean> cycleStatuses = new ArrayList<Boolean>();
		boolean successfulCycle = false;
		String sd1 = "abcdefg";
		IMessage m1 = new Message(sequenceNumber, null, null, sd1);
		for (;sequenceNumber < successfulCyclesToWait;) {
			if (GeneralTestUtilityClass.generateRandomNumber(1, 5) < 3) {
				successfulCycle = false;
			} else {
				successfulCycle = true;
			}
			if (cycleStatuses.size() == 0 || cycleStatuses.get(cycleStatuses.size()-1)) {
				sd1 = "abcdefg";
				m1 = new Message(sequenceNumber, null, null, sd1);
				
				Assertions.assertTrue(sb.isEmpty());
				BufferUtilityClass.assertMessageSent(sb, sequenceNumber, m1);
				Assertions.assertFalse(sb.isEmpty());
				
				BufferUtilityClass.assertInputStoredEquals(this.receiverConn.getInputStream(), serialiser.serialise(m1).getBytes());
				
				Assertions.assertTrue(sb.isBlocked());
				Assertions.assertFalse(sb.isEmpty());
			}
			cycleStatuses.add(successfulCycle);
			
			if (!successfulCycle) {
				GeneralTestUtilityClass.performWait(timeoutDuration);
				
				Assertions.assertEquals(sequenceNumber, sb.getCurrentSequenceNumber());
				
//				BufferUtilityClass.assertInputStoredEquals(this.receiverConn.getInputStream(), serialiser.serialise(m1).getBytes());
				
//				GeneralTestUtilityClass.performWait(timeoutDuration);
			} else {
				GeneralTestUtilityClass.performWait(waitDuration);
				Assertions.assertFalse(sb.isEmpty());
				BufferUtilityClass.assertAcknowledgementOfMessageReceived(sb, m1);
				Assertions.assertTrue(sb.isEmpty());
				sequenceNumber++;
			}
			
			while (sb.hasRunningTimer()) {
				
			}
		}
		System.out.print("Successful with: " + "[");
		for (int i = 0; i < cycleStatuses.size() - 1; i++) {
			System.out.print(cycleStatuses.get(i) + ",");
		}
		System.out.println(cycleStatuses.get(cycleStatuses.size() - 1) + "]");
	}
}
