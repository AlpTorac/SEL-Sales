package test.external.acknowledgement;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.acknowledgement.IAcknowledger;
import external.acknowledgement.StandardAcknowledger;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyConnection;
@Execution(value = ExecutionMode.SAME_THREAD)
class StandardAcknowledgerTest {
	private DummyConnection conn;
	private IAcknowledger acknowledger;
	private int sequenceNumber;
	private MessageContext context;
	private MessageFlag[] flags;
	private String serialisedData;
	private IMessage message;
	
	@BeforeEach
	void prep() {
		this.conn = new DummyConnection("clientaddress");
		this.acknowledger = new StandardAcknowledger(conn);
		sequenceNumber = 1;
		context = MessageContext.MENU;
		flags = new MessageFlag[] {};
		serialisedData = "menuData";
		message = new Message(sequenceNumber, context, flags, serialisedData);
	}
	
	@AfterEach
	void cleanUp() {
		try {
			this.conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void acknowledgeTest() {
		ByteArrayOutputStream os = conn.getOutputStream();
		Assertions.assertTrue(this.acknowledger.acknowledge(message));
		IMessageSerialiser ms = new MessageSerialiser(new StandardMessageFormat());
		String serialisedAcknowledgementMessage = ms.serialise(message.getMinimalAcknowledgementMessage());
		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedAcknowledgementMessage.getBytes());
	}
}
