package test.external.handler;

import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import controller.IController;
import external.handler.OrderHandler;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import external.message.MessageFormat;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import external.message.StandardMessageParser;
import test.external.dummy.DummyConnection;
import test.external.dummy.DummyServerController;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderHandlerTest extends MessageHandlerSuperClass {
	private MessageFormat format = new StandardMessageFormat();
	private String fieldSeparator = format.getDataFieldSeparatorForString();
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageParser parser;
	private IController controller;
	private DummyConnection senderConn;
	private DummyConnection receiverConn;
	private boolean isOrderReceivedByController;
	
	@BeforeEach
	void prep() {
		senderConn = new DummyConnection("DeviceAddress");
		receiverConn = new DummyConnection("receiverAddress");
		senderConn.setInputTarget(receiverConn.getInputStream());
		parser = new StandardMessageParser();
		controller = initDummyController();
		setHandler(new OrderHandler(parser, controller));
		isOrderReceivedByController = false;
	}
	
	@AfterEach
	void cleanUp() {
		try {
			senderConn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.isOrderReceivedByController = false;
	}
	
	private IController initDummyController() {
		DummyServerController controller = new DummyServerController() {
			@Override
			public void addOrder(String serialisedOrder) {isOrderReceivedByController = true;}
		};
		return controller;
	}
	
	@Test
	void handleMessageSuccessfulTest() {
		IMessage message = new Message(MessageContext.ORDER, null, null);
		this.handleMessageSuccessfulTest(serialiser.serialise(message));
	}
	
	@Test
	void handleMessageFailTest() {
		IMessage message = new Message(MessageContext.MENU, null, null);
		this.handleMessageFailTest(serialiser.serialise(message));
		
		IMessage message2 = new Message(MessageContext.ORDER, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, null);
		this.handleMessageFailTest(serialiser.serialise(message2));
	}
	
	@Test
	void verificationSuccessfulTest() {
		IMessage message = new Message(MessageContext.ORDER, null, null);
		this.verificationSuccessfulTest(message);
	}
	
	@Test
	void verificationFailTest() {
		IMessage message = new Message(MessageContext.ORDER, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, null);
		this.verificationFailTest(message);
		
		IMessage message2 = new Message(MessageContext.MENU, null, null);
		this.verificationFailTest(message2);
		
		IMessage message3 = new Message(null, null, null);
		this.verificationFailTest(message3);
	}
	
	@Test
	void actionSuccessfulTest() {
		int sequenceNumber = 5;
		MessageContext context = MessageContext.ORDER;
		MessageFlag[] flags = null;
		String serialisedData = null;
		IMessage message = new Message(sequenceNumber, context, flags, serialisedData);
		this.actionSuccessfulTest(message);
		Assertions.assertTrue(isOrderReceivedByController);
	}
}
