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

class StandardMessageParserTest {
	private IMessageParser messageParser;
	private MessageFormat messageFormat = new StandardMessageFormat();
	
	@BeforeEach
	void prep() {
		this.messageParser = new StandardMessageParser();
	}
	
	@Test
	void parseMultiFlagTest() {
		int sequenceNumber = 1;
		String start = messageFormat.getMessageStart();
		String context = "menu";
		String fieldSeparator = messageFormat.getDataFieldSeparatorForString();
		String flags = "ack,ack";
		String serialisedData = "menuData";
		String end = messageFormat.getMessageEnd();
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
		String start = messageFormat.getMessageStart();
		String context = "menu";
		String fieldSeparator = messageFormat.getDataFieldSeparatorForString();
		String flags = "";
		String serialisedData = "menuData";
		String end = messageFormat.getMessageEnd();
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, MessageContext.MENU, null, serialisedData, parsedMessage);
	}
	
	@Test
	void parseMenuDataAckMessageTest() {
		int sequenceNumber = 1;
		String start = messageFormat.getMessageStart();
		String context = "menu";
		String fieldSeparator = messageFormat.getDataFieldSeparatorForString();
		String flags = "ack";
		String serialisedData = "";
		String end = messageFormat.getMessageEnd();
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, MessageContext.MENU, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, serialisedData, parsedMessage);
	}
	
	@Test
	void parseOrderDataMessageTest() {
		int sequenceNumber = 1;
		String start = messageFormat.getMessageStart();
		String context = "order";
		String fieldSeparator = messageFormat.getDataFieldSeparatorForString();
		String flags = "";
		String serialisedData = "orderData";
		String end = messageFormat.getMessageEnd();
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, MessageContext.ORDER, null, serialisedData, parsedMessage);
	}
	
	@Test
	void parseOrderDataAckMessageTest() {
		int sequenceNumber = 1;
		String start = messageFormat.getMessageStart();
		String context = "order";
		String fieldSeparator = messageFormat.getDataFieldSeparatorForString();
		String flags = "ack";
		String serialisedData = "";
		String end = messageFormat.getMessageEnd();
		String serialisedMessage = start+sequenceNumber+fieldSeparator+context+fieldSeparator+flags+fieldSeparator+serialisedData+end;
		IMessage parsedMessage = this.messageParser.parseMessage(serialisedMessage);
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, MessageContext.ORDER, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, serialisedData, parsedMessage);
	}
}
