package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.connection.BasicMessageSender;
import external.connection.IMessageSendingStrategy;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageFormat;
import external.message.StandardMessageFormat;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyConnection;
@Execution(value = ExecutionMode.SAME_THREAD)
class BasicMessageSenderTest {
	private DummyConnection conn;
	private IMessageSendingStrategy mss;
	private MessageFormat messageFormat;
	private String fieldSeparator;
	private String fieldElementSeparator;
	
	@BeforeEach
	void prep() {
		conn = new DummyConnection("clientaddress");
		mss = new BasicMessageSender();
		this.messageFormat = new StandardMessageFormat();
		this.fieldSeparator = this.messageFormat.getDataFieldSeparatorForString();
		this.fieldElementSeparator = this.messageFormat.getDataFieldElementSeparatorForString();
	}
	
	@AfterEach
	void cleanUp() {
		try {
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void sendMessageTest() {
		String serialisedData = "abcdefg";
		IMessage message = new Message(null, null, serialisedData);
		ByteArrayOutputStream os = conn.getOutputStream();
		Assertions.assertTrue(mss.sendMessage(conn, message));
		String serialisedMessage = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedMessage.getBytes());
	}
	
	@Test
	void sendMultipleMessagesTest() {
		String serialisedData = "abcdefg";
		IMessage message = new Message(null, null, serialisedData);
		ByteArrayOutputStream os = conn.getOutputStream();
		Assertions.assertTrue(mss.sendMessage(conn, message));
		String serialisedMessage1 = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedMessage1.getBytes());
		
		serialisedData = "gdfkhjlgs";
		message = new Message(null, null, serialisedData);
		os = conn.getOutputStream();
		Assertions.assertTrue(mss.sendMessage(conn, message));
		String serialisedMessage2 = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedMessage2.getBytes());
	}
}
