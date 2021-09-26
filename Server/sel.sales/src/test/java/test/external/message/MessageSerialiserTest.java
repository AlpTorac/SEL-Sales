package test.external.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import external.message.MessageFormat;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;

class MessageSerialiserTest {
	private IMessageSerialiser messageSerialiser;
	private MessageFormat messageFormat;
	private String fieldSeparator;
	private String fieldElementSeparator;
	
	@BeforeEach
	void prep() {
		this.messageFormat = new StandardMessageFormat();
		this.messageSerialiser = new MessageSerialiser(this.messageFormat);
		this.fieldSeparator = this.messageFormat.getDataFieldSeparatorForString();
		this.fieldElementSeparator = this.messageFormat.getDataFieldElementSeparatorForString();
	}
	
	@Test
	void multiFlagSerialiseTest() {
		int sequenceNumber = 1;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT, MessageFlag.ACKNOWLEDGEMENT};
		String serialisedData = "menuData";
		IMessage message = new Message(sequenceNumber,context,flags,serialisedData);
		String serialisedMessage = this.messageSerialiser.serialise(message);
		Assertions.assertEquals(messageFormat.getMessageStart() +
				sequenceNumber +
				fieldSeparator +
				context.toString() + 
				fieldSeparator +
				flags[0].toString() +
				fieldElementSeparator +
				flags[1].toString() +
				fieldSeparator +
				serialisedData +
				messageFormat.getMessageEnd(), serialisedMessage);
	}
	
	@Test
	void serialiseMenuDataTest() {
		int sequenceNumber = 1;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "menuData";
		IMessage message = new Message(sequenceNumber,context,flags,serialisedData);
		String serialisedMessage = this.messageSerialiser.serialise(message);
		Assertions.assertEquals(messageFormat.getMessageStart() +
				sequenceNumber +
				fieldSeparator +
				context.toString() + 
				fieldSeparator +
				fieldSeparator +
				serialisedData +
				messageFormat.getMessageEnd(), serialisedMessage);
	}
	
	@Test
	void serialiseMenuDataAckTest() {
		int sequenceNumber = 1;
		MessageContext context = MessageContext.MENU;
		MessageFlag[] flags = new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT};
		String serialisedData = "";
		IMessage message = new Message(sequenceNumber,context,flags,serialisedData);
		String serialisedMessage = this.messageSerialiser.serialise(message);
		Assertions.assertEquals(messageFormat.getMessageStart() +
				sequenceNumber +
				fieldSeparator +
				context.toString() + 
				fieldSeparator +
				flags[0].toString() +
				fieldSeparator +
				serialisedData +
				messageFormat.getMessageEnd(), serialisedMessage);
	}
	
	@Test
	void serialiseOrderDataTest() {
		int sequenceNumber = 1;
		MessageContext context = MessageContext.ORDER;
		MessageFlag[] flags = new MessageFlag[] {};
		String serialisedData = "orderData";
		IMessage message = new Message(sequenceNumber,context,flags,serialisedData);
		String serialisedMessage = this.messageSerialiser.serialise(message);
		Assertions.assertEquals(messageFormat.getMessageStart() +
				sequenceNumber +
				fieldSeparator +
				context.toString() + 
				fieldSeparator +
				fieldSeparator +
				serialisedData +
				messageFormat.getMessageEnd(), serialisedMessage);
	}
	
	@Test
	void serialiseOrderDataAckTest() {
		int sequenceNumber = 1;
		MessageContext context = MessageContext.ORDER;
		MessageFlag[] flags = new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT};
		String serialisedData = "";
		IMessage message = new Message(sequenceNumber,context,flags,serialisedData);
		String serialisedMessage = this.messageSerialiser.serialise(message);
		Assertions.assertEquals(messageFormat.getMessageStart() +
				sequenceNumber +
				fieldSeparator +
				context.toString() + 
				fieldSeparator +
				flags[0].toString() +
				fieldSeparator +
				serialisedData +
				messageFormat.getMessageEnd(), serialisedMessage);
	}
}
