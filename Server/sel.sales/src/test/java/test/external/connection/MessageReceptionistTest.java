package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import external.connection.incoming.IMessageReceptionist;
import external.connection.incoming.MessageReceptionist;
import external.connection.outgoing.BasicMessageSender;
import external.connection.outgoing.ISendBuffer;
import external.connection.outgoing.StandardSendBuffer;
import external.connection.pingpong.IPingPong;
import external.connection.timeout.FixTimeoutStrategy;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import test.GeneralTestUtilityClass;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyController;
import test.external.dummy.DummyPingPong;
@Execution(value = ExecutionMode.SAME_THREAD)
class MessageReceptionistTest {
	private long waitTime = 300;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IController controller;
	private DummyConnection conn;
	private ISendBuffer buffer;
	private IPingPong pingPong;
	private ExecutorService es;
	private boolean isOrderReceivedByController;
	
	private int resendLimit = 5;
	
	private IMessageReceptionist listener;
	
	@BeforeEach
	void prep() {
		isOrderReceivedByController = false;
		conn = new DummyConnection("clientaddress");
		controller = initController();
		es = Executors.newCachedThreadPool();
		buffer = new StandardSendBuffer(conn, es);
		pingPong = new DummyPingPong(conn, new BasicMessageSender(), new FixTimeoutStrategy(10000, ChronoUnit.MILLIS), es, resendLimit);
		listener = new MessageReceptionist(conn, controller, buffer, pingPong, es);
	}
	
	@AfterEach
	void cleanUp() {
		try {
			buffer.close();
			conn.close();
			listener.close();
			pingPong.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			es.awaitTermination(waitTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		isOrderReceivedByController = false;
	}
	
	private IController initController() {
		DummyController controller = new DummyController() {
			@Override
			public void addOrder(String serialisedOrder) {isOrderReceivedByController = true;}
		};
		return controller;
	}
	
	@Test
	void handleOrderDataMessageTest() {
		int sequenceNumber = 0;
		MessageContext context = MessageContext.ORDER;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "someOrderData";
		IMessage orderDataMessage = new Message(sequenceNumber, context, flags, serialisedData);
		String serialisedMessage = serialiser.serialise(orderDataMessage);
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialisedMessage);
		ByteArrayOutputStream os = conn.getOutputStream();
		Assertions.assertTrue(listener.checkForMessages());
		
		String serialisedAckMessage = serialiser.serialise(orderDataMessage.getMinimalAcknowledgementMessage());
		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedAckMessage.getBytes());
		Assertions.assertTrue(isOrderReceivedByController);
	}

	@Test
	void handleOrderAcknowledgementMessageTest() {
		IMessage m = new Message(null, null, null);
		buffer.addMessage(m);
		Assertions.assertTrue(buffer.sendMessage());
		
		int sequenceNumber = 0;
		MessageContext context = MessageContext.ORDER;
		MessageFlag[] flags = new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT};
		String serialisedData = "";
		IMessage orderAckMessage = new Message(sequenceNumber, context, flags, serialisedData);
		String serialisedMessage = serialiser.serialise(orderAckMessage);
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialisedMessage);
		Assertions.assertTrue(listener.checkForMessages());
		
		Assertions.assertFalse(buffer.isBlocked());
	}

	@Test
	void handleMenuAcknowledgementMessageTest() {
		IMessage m = new Message(null, null, null);
		buffer.addMessage(m);
		Assertions.assertTrue(buffer.sendMessage());
		
		int sequenceNumber = 0;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT};
		String serialisedData = "";
		IMessage menuAckMessage = new Message(sequenceNumber, context, flags, serialisedData);
		String serialisedMessage = serialiser.serialise(menuAckMessage);
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialisedMessage);
		Assertions.assertTrue(listener.checkForMessages());
		
		Assertions.assertFalse(buffer.isBlocked());
	}
	
	@Test
	void unhandleableMessageTest() {
		IMessage m = new Message(null, null, null);
		String serialisedMessage = serialiser.serialise(m);
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), serialisedMessage);
		Assertions.assertFalse(listener.checkForMessages());
	}
	
	@Test
	void handlePingPongMessageTest() {
		Assertions.assertTrue(pingPong.start());
		pingPong.timeout();
		Assertions.assertTrue(resendLimit - 1 == pingPong.getRemainingResendTries());
		
		MessageContext context = MessageContext.PINGPONG;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "";
		IMessage m = new Message(context, flags, serialisedData);
		String sm = serialiser.serialise(m);
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), sm);
		Assertions.assertTrue(listener.checkForMessages());
		
		Assertions.assertTrue(resendLimit == pingPong.getRemainingResendTries());
	}
	
}
