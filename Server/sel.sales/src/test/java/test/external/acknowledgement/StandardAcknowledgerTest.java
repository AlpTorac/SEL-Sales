package test.external.acknowledgement;

import java.io.IOException;
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
//@Execution(value = ExecutionMode.SAME_THREAD)
class StandardAcknowledgerTest {
	private DummyConnection senderConn;
	private DummyConnection receiverConn;
	private IAcknowledger acknowledger;
	private int sequenceNumber;
	private MessageContext context;
	private MessageFlag[] flags;
	private String serialisedData;
	private IMessage message;
	
	@BeforeEach
	void prep() {
		this.senderConn = new DummyConnection("DeviceAddress");
		this.receiverConn = new DummyConnection("senderAddress");
		this.senderConn.setInputTarget(this.receiverConn.getInputStream());
		this.acknowledger = new StandardAcknowledger(senderConn);
		sequenceNumber = 1;
		context = MessageContext.MENU;
		flags = new MessageFlag[] {};
		serialisedData = "menuData";
		message = new Message(sequenceNumber, context, flags, serialisedData);
	}
	
	@AfterEach
	void cleanUp() {
		try {
			this.senderConn.close();
			this.receiverConn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@Test
	void acknowledgeTest() {
		Assertions.assertTrue(this.acknowledger.acknowledge(message));
		IMessageSerialiser ms = new MessageSerialiser(new StandardMessageFormat());
		String serialisedAcknowledgementMessage = ms.serialise(message.getMinimalAcknowledgementMessage());
		BufferUtilityClass.assertInputStoredEquals(this.receiverConn.getInputStream(), serialisedAcknowledgementMessage.getBytes());
	}
}
