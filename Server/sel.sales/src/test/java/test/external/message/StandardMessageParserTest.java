package test.external.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.message.IMessage;
import external.message.IMessageParser;
import external.message.MessageContext;
import external.message.MessageFlag;
import external.message.MessageFormat;
import external.message.StandardMessageFormat;
import external.message.StandardMessageParser;
import model.exceptions.NoSuchMessageContextException;
import model.exceptions.NoSuchMessageFlagException;

class StandardMessageParserTest {
	private IMessageParser messageParser;
	private MessageFormat messageFormat = new StandardMessageFormat();
	private String fieldSeparator = messageFormat.getDataFieldSeparatorForString();
	private String fieldElementSeparator = messageFormat.getDataFieldElementSeparatorForString();
	private String start = messageFormat.getMessageStart();
	private String end = messageFormat.getMessageEnd();
	@BeforeEach
	void prep() {
		this.messageParser = new StandardMessageParser();
	}
	
	@Test
	void parseMultiFlagTest() {
		int sequenceNumber = 1;
		String context = "menu";
		String flags = "ack,ack";
		String serialisedData = "menuData";
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber,
				MessageContext.MENU,
				new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT,MessageFlag.ACKNOWLEDGEMENT},
				serialisedData,
				parsedMessage);
	}
	
	@Test
	void parseMenuDataMessageTest() {
		int sequenceNumber = 1;
		String context = "menu";
		String flags = "";
		String serialisedData = "menuData";
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, MessageContext.MENU, null, serialisedData, parsedMessage);
	}
	
	@Test
	void parseMenuDataAckMessageTest() {
		int sequenceNumber = 1;
		String context = "menu";
		String flags = "ack";
		String serialisedData = "";
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, MessageContext.MENU, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, serialisedData, parsedMessage);
	}
	
	@Test
	void parseOrderDataMessageTest() {
		int sequenceNumber = 1;
		String context = "order";
		String flags = "";
		String serialisedData = "orderData";
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, MessageContext.ORDER, null, serialisedData, parsedMessage);
	}
	
	@Test
	void parseOrderDataAckMessageTest() {
		int sequenceNumber = 1;
		String context = "order";
		String flags = "ack";
		String serialisedData = "";
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, MessageContext.ORDER, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, serialisedData, parsedMessage);
	}
	
	@Test
	void emptyFieldNoExceptionTest() {
		int sequenceNumber = 0;
		String context = "order";
		String flags = "ack";
		String data = "someData";
		this.messageParser.parseMessage(start + fieldSeparator + fieldSeparator + fieldSeparator + end);
		
		this.messageParser.parseMessage(start + sequenceNumber + fieldSeparator + fieldSeparator + fieldSeparator + end);
		this.messageParser.parseMessage(start + sequenceNumber + fieldSeparator + context + fieldSeparator + fieldSeparator + end);
		this.messageParser.parseMessage(start + sequenceNumber + fieldSeparator + fieldSeparator + flags + fieldSeparator + end);
		this.messageParser.parseMessage(start + sequenceNumber + fieldSeparator + fieldSeparator + fieldSeparator + data + end);
		this.messageParser.parseMessage(start + sequenceNumber + fieldSeparator + context + fieldSeparator + flags + fieldSeparator + end);
		this.messageParser.parseMessage(start + sequenceNumber + fieldSeparator + context + fieldSeparator + fieldSeparator + data + end);
		this.messageParser.parseMessage(start + sequenceNumber + fieldSeparator + fieldSeparator + flags + fieldSeparator + data + end);
		
		this.messageParser.parseMessage(start + fieldSeparator + context + fieldSeparator + fieldSeparator + end);
		this.messageParser.parseMessage(start + fieldSeparator + context + fieldSeparator + flags + fieldSeparator + end);
		this.messageParser.parseMessage(start + fieldSeparator + context + fieldSeparator + fieldSeparator + data + end);
		this.messageParser.parseMessage(start + fieldSeparator + context + fieldSeparator + flags + fieldSeparator + data + end);
		
		this.messageParser.parseMessage(start + fieldSeparator + fieldSeparator + flags + fieldSeparator + end);
		this.messageParser.parseMessage(start + fieldSeparator + fieldSeparator + flags + fieldSeparator + data + end);
		
		this.messageParser.parseMessage(start + fieldSeparator + fieldSeparator + fieldSeparator + data + end);
		
		this.messageParser.parseMessage(start + sequenceNumber + fieldSeparator + context + fieldSeparator + flags + fieldSeparator + data + end);
	}
	
	@Test
	void wrongFormatTest() {
		Assertions.assertThrows(NumberFormatException.class, () -> {
			this.messageParser.parseMessage(start + "dfhkjggkhjgdfg" + fieldSeparator + fieldSeparator + fieldSeparator + end);
		});
		Assertions.assertThrows(NoSuchMessageContextException.class, () -> {
			this.messageParser.parseMessage(start + fieldSeparator + "hjigdfjkhgsdf" + fieldSeparator + fieldSeparator + end);
		});
		Assertions.assertThrows(NoSuchMessageFlagException.class, () -> {
			this.messageParser.parseMessage(start + fieldSeparator + fieldSeparator + "gfhsdagf" + fieldSeparator + end);
		});
		Assertions.assertThrows(NoSuchMessageFlagException.class, () -> {
			this.messageParser.parseMessage(start + fieldSeparator + fieldSeparator + "ack"+fieldElementSeparator+"gfhsdagf" + fieldSeparator + end);
		});
		Assertions.assertThrows(NoSuchMessageFlagException.class, () -> {
			this.messageParser.parseMessage(start + fieldSeparator + fieldSeparator + "fdsajhg"+fieldElementSeparator+"ack" + fieldSeparator + end);
		});
	}
}
