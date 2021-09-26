package test.external.acknowledgement;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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

class StandardAcknowledgerTest {
	private ByteArrayOutputStream os;
	private IAcknowledger acknowledger;
	private int sequenceNumber;
	private MessageContext context;
	private MessageFlag[] flags;
	private String serialisedData;
	private IMessage message;
	
	@BeforeEach
	void prep() {
		this.os = new ByteArrayOutputStream();
		this.acknowledger = new StandardAcknowledger(os);
		sequenceNumber = 1;
		context = MessageContext.MENU;
		flags = new MessageFlag[] {};
		serialisedData = "menuData";
		message = new Message(sequenceNumber, context, flags, serialisedData);
	}
	
	@AfterEach
	void cleanUp() {
		try {
			this.os.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void acknowledgeTest() {
		Assertions.assertTrue(this.acknowledger.acknowledge(message));
		IMessageSerialiser ms = new MessageSerialiser(new StandardMessageFormat());
		String serialisedAcknowledgementMessage = ms.serialise(message.getMinimalAcknowledgementMessage());
		BufferUtilityClass.assertOutputWrittenEquals(os, serialisedAcknowledgementMessage.getBytes());
	}
}
