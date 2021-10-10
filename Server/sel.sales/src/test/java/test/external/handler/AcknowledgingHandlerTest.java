package test.external.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import controller.IController;
import external.acknowledgement.MinimalAcknowledgementStrategy;
import external.acknowledgement.StandardAcknowledger;
import external.connection.outgoing.BasicMessageSender;
import external.connection.outgoing.StandardSendBuffer;
import external.handler.AcknowledgementHandler;
import external.handler.AcknowledgingHandler;
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
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyConnection;

class AcknowledgingHandlerTest extends MessageHandlerTest {

	private MessageFormat format = new StandardMessageFormat();
	private String fieldSeparator = format.getDataFieldSeparatorForString();
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageParser parser;
	private DummyConnection senderConn;
	private DummyConnection receiverConn;
	
	@BeforeEach
	void prep() {
		senderConn = new DummyConnection("clientAddress");
		receiverConn = new DummyConnection("receiverAddress");
		senderConn.setInputTarget(receiverConn.getInputStream());
		parser = new StandardMessageParser();
		setHandler(new AcknowledgingHandler(parser, new StandardAcknowledger(senderConn)));
	}
	
	@AfterEach
	void cleanUp() {
		try {
			senderConn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setHandler(null);
	}
	
	@Test
	void verificationSuccessfulTest() {
		IMessage message2 = new Message(MessageContext.MENU, null, null);
		this.verificationSuccessfulTest(message2);
		
		IMessage message3 = new Message(null, null, null);
		this.verificationSuccessfulTest(message3);
		
		IMessage message4 = new Message(MessageContext.ORDER, null, null);
		this.verificationSuccessfulTest(message4);
	}
	
	@Test
	void verificationFailTest() {
		IMessage message = new Message(MessageContext.ORDER, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, null);
		this.verificationFailTest(message);
		
		IMessage message2 = new Message(MessageContext.PINGPONG, null, null);
		this.verificationFailTest(message2);
	}
	
	@Test
	void acknowledgementSuccessfulTest() {
		int sequenceNumber = 5;
		MessageContext context = MessageContext.ORDER;
		MessageFlag[] flags = null;
		String serialisedData = "";
		IMessage message = new Message(sequenceNumber, context, flags, serialisedData);
		this.actionSuccessfulTest(message);
		String serialisedExpected = (format.getMessageStart() + sequenceNumber + fieldSeparator +
				context.toString() + fieldSeparator + MessageFlag.ACKNOWLEDGEMENT.toString() +
				fieldSeparator + serialisedData + format.getMessageEnd());
		BufferUtilityClass.assertInputStoredEquals(this.receiverConn.getInputStream(), serialisedExpected.getBytes());
	}
	
}
