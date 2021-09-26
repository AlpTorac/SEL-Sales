package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.connection.BasicMessageSender;
import external.connection.IMessageSendingStrategy;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageFormat;
import external.message.StandardMessageFormat;
import test.external.buffer.BufferUtilityClass;

class BasicMessageSenderTest {
	private ByteArrayOutputStream os;
	private IMessageSendingStrategy mss;
	private MessageFormat messageFormat;
	private String fieldSeparator;
	private String fieldElementSeparator;
	
	@BeforeEach
	void prep() {
		os = new ByteArrayOutputStream();
		mss = new BasicMessageSender();
		this.messageFormat = new StandardMessageFormat();
		this.fieldSeparator = this.messageFormat.getDataFieldSeparatorForString();
		this.fieldElementSeparator = this.messageFormat.getDataFieldElementSeparatorForString();
	}
	
	@AfterEach
	void cleanUp() {
		try {
			os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void sendMessageTest() {
		String serialisedData = "abcdefg";
		IMessage message = new Message(null, null, serialisedData);
		Assertions.assertTrue(mss.sendMessage(os, message));
		String serialisedMessage = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedMessage.getBytes());
	}
	
	@Test
	void sendMultipleMessagesTest() {
		String serialisedData = "abcdefg";
		IMessage message = new Message(null, null, serialisedData);
		Assertions.assertTrue(mss.sendMessage(os, message));
		String serialisedMessage1 = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedMessage1.getBytes());
		
		serialisedData = "gdfkhjlgs";
		message = new Message(null, null, serialisedData);
		Assertions.assertTrue(mss.sendMessage(os, message));
		String serialisedMessage2 = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
		BufferUtilityClass.assertOutputWrittenEquals(os, (serialisedMessage1+serialisedMessage2).getBytes());
	}
}
