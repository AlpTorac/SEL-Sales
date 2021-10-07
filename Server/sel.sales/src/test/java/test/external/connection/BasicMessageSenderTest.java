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

import external.connection.outgoing.BasicMessageSender;
import external.connection.outgoing.IMessageSendingStrategy;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageFormat;
import external.message.StandardMessageFormat;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyConnection;
@Execution(value = ExecutionMode.SAME_THREAD)
class BasicMessageSenderTest {
	private DummyConnection senderConn;
	private DummyConnection receiverConn;
	private IMessageSendingStrategy mss;
	private MessageFormat messageFormat;
	private String fieldSeparator;
	private String fieldElementSeparator;
	
	@BeforeEach
	void prep() {
		senderConn = new DummyConnection("clientaddress");
		receiverConn = new DummyConnection("receiverAddress");
		senderConn.setInputTarget(receiverConn.getInputStream());
		mss = new BasicMessageSender();
		this.messageFormat = new StandardMessageFormat();
		this.fieldSeparator = this.messageFormat.getDataFieldSeparatorForString();
		this.fieldElementSeparator = this.messageFormat.getDataFieldElementSeparatorForString();
	}
	
	@AfterEach
	void cleanUp() {
		try {
			senderConn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void sendMessageTest() {
		String serialisedData = "abcdefg";
		IMessage message = new Message(null, null, serialisedData);
//		ByteArrayOutputStream os = senderConn.getOutputStream();
		Assertions.assertTrue(mss.sendMessage(senderConn, message));
		String serialisedMessage = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
//		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedMessage.getBytes());
		BufferUtilityClass.assertInputStoredEquals(this.receiverConn.getInputStream(), serialisedMessage.getBytes());
	}
	
	@Test
	void sendMultipleMessagesTest() {
		String serialisedData = "abcdefg";
		IMessage message = new Message(null, null, serialisedData);
//		ByteArrayOutputStream os = conn.getOutputStream();
		Assertions.assertTrue(mss.sendMessage(senderConn, message));
		String serialisedMessage1 = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
//		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedMessage1.getBytes());
		BufferUtilityClass.assertInputStoredEquals(this.receiverConn.getInputStream(), serialisedMessage1.getBytes());
		
		serialisedData = "gdfkhjlgs";
		message = new Message(null, null, serialisedData);
//		os.reset();
		Assertions.assertTrue(mss.sendMessage(senderConn, message));
		String serialisedMessage2 = (messageFormat.getMessageStart()+"0"+fieldSeparator +
				fieldSeparator + fieldSeparator +
				message.getSerialisedData() + messageFormat.getMessageEnd());
//		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedMessage2.getBytes());
		BufferUtilityClass.assertInputStoredEquals(this.receiverConn.getInputStream(), serialisedMessage2.getBytes());
	}
}
