package test.external.handler;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.buffer.ISendBuffer;
import external.buffer.StandardSendBuffer;
import external.handler.AcknowledgementHandler;
import external.message.IMessage;
import external.message.IMessageParser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import external.message.StandardMessageParser;

class AcknowledgementHandlerTest extends MessageHandlerTest {
	private MessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageParser parser;
	private ISendBuffer buffer;
	private ExecutorService es;
	private OutputStream os;
	
	@BeforeEach
	void prep() {
		es = Executors.newFixedThreadPool(3);
		os = new ByteArrayOutputStream();
		parser = new StandardMessageParser();
		buffer = new StandardSendBuffer(os, es);
		setHandler(new AcknowledgementHandler(parser, buffer));
	}
	
	@AfterEach
	void cleanUp() {
		es.shutdown();
		try {
			os.close();
			buffer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		setHandler(null);
	}
	
	@Test
	void handleMessageSuccessfulTest() {
		buffer.addMessage(new Message(null, null, null));
		Assertions.assertTrue(buffer.sendMessage());
		Assertions.assertTrue(buffer.isBlocked());
		
		int sequenceNumber = 0;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "someMenuData";
		IMessage message = new Message(sequenceNumber, context, flags, serialisedData);
		IMessage ackMessage = message.getMinimalAcknowledgementMessage();
		this.handleMessageSuccessfulTest(serialiser.serialise(ackMessage));
		Assertions.assertFalse(buffer.isBlocked());
	}
	
	@Test
	void handleMessageFailTest() {
		buffer.addMessage(new Message(null, null, null));
		Assertions.assertTrue(buffer.sendMessage());
		Assertions.assertTrue(buffer.isBlocked());
		
		int sequenceNumber = 0;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "someMenuData";
		IMessage message = new Message(sequenceNumber, context, flags, serialisedData);
		this.handleMessageFailTest(serialiser.serialise(message));
		Assertions.assertTrue(buffer.isBlocked());
	}
	
	@Test
	void verificationSuccessfulTest() {
		int sequenceNumber = 3;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "someMenuData";
		IMessage message = new Message(sequenceNumber, context, flags, serialisedData);
		IMessage ackMessage = message.getMinimalAcknowledgementMessage();
		this.verificationSuccessfulTest(ackMessage);
	}
	
	@Test
	void verificationFailTest() {
		int sequenceNumber = 3;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "someMenuData";
		IMessage message = new Message(sequenceNumber, context, flags, serialisedData);
		this.verificationFailTest(message);
	}
	
	@Test
	void acknowledgementTest() {
		IMessage message = new Message(0, null, null, null);
		this.acknowledgementSuccessfulTest(message);
	}
	
	@Test
	void actionSuccessfulTest() {
		buffer.addMessage(new Message(null, null, null));
		Assertions.assertTrue(buffer.sendMessage());
		Assertions.assertTrue(buffer.isBlocked());
		
		int sequenceNumber = 0;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "someMenuData";
		IMessage message = new Message(sequenceNumber, context, flags, serialisedData);
		IMessage ackMessage = message.getMinimalAcknowledgementMessage();
		this.actionSuccessfulTest(ackMessage);
	}
	
	@Test
	void actionFailTest() {
		buffer.addMessage(new Message(null, null, null));
		Assertions.assertTrue(buffer.sendMessage());
		Assertions.assertTrue(buffer.isBlocked());
		
		int sequenceNumber = 0;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "someMenuData";
		IMessage message = new Message(sequenceNumber, context, flags, serialisedData);
		this.actionFailTest(message);
	}
}
