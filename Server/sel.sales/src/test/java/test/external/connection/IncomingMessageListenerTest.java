package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.IController;
import external.buffer.ISendBuffer;
import external.buffer.StandardSendBuffer;
import external.connection.IIncomingMessageListener;
import external.connection.IncomingMessageListener;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyController;

class IncomingMessageListenerTest {
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IController controller;
	private byte[] isBuffer;
	private ByteArrayInputStream is;
	private ByteArrayOutputStream os;
	private ISendBuffer buffer;
	private ExecutorService es;
	private boolean isOrderReceivedByController;
	
	private IIncomingMessageListener listener;
	
	@BeforeEach
	void prep() {
		isOrderReceivedByController = false;
		isBuffer = new byte[100];
		is = new ByteArrayInputStream(isBuffer);
		os = new ByteArrayOutputStream();
		controller = initController();
		es = Executors.newFixedThreadPool(3);
		buffer = new StandardSendBuffer(os, es);
		listener = new IncomingMessageListener(is, os, controller, buffer);
	}
	
	@AfterEach
	void cleanUp() {
		try {
			buffer.close();
			is.close();
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		es.shutdown();
		isOrderReceivedByController = false;
	}
	
	private void fillBuffer(String bufferContent) {
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
		fillBuffer(serialisedMessage);
		
		Assertions.assertTrue(listener.handleCurrentMessage());
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
		fillBuffer(serialisedMessage);
		
		Assertions.assertTrue(listener.handleCurrentMessage());
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
		fillBuffer(serialisedMessage);
		
		Assertions.assertTrue(listener.handleCurrentMessage());
		Assertions.assertFalse(buffer.isBlocked());
	}
	
	@Test
	void unhandleableMessageTest() {
		IMessage m = new Message(null, null, null);
		String serialisedMessage = serialiser.serialise(m);
		fillBuffer(serialisedMessage);
		Assertions.assertFalse(listener.handleCurrentMessage());
	}
	
}
