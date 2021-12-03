package test.external.acknowledgement;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import external.acknowledgement.IAcknowledgementStrategy;
import external.acknowledgement.MinimalAcknowledgementStrategy;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import test.external.message.MessageTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class MinimalAcknowledgementStrategyTest {
	private IAcknowledgementStrategy acknowledgementStrategy;
	private int sequenceNumber;
	private MessageContext context;
	private MessageFlag[] flags;
	private String serialisedData;
	private IMessage message;
	
	@BeforeEach
	void prep() {
		acknowledgementStrategy = new MinimalAcknowledgementStrategy();
		sequenceNumber = 1;
		context = MessageContext.MENU;
		flags = new MessageFlag[] {};
		serialisedData = "menuData";
		message = new Message(sequenceNumber, context, flags, serialisedData);
	}
	
	@Test
	void acknowledgeTest() {
		IMessage expectedAck = message.getMinimalAcknowledgementMessage();
		MessageTestUtilityClass.assertMessageEquals(expectedAck, this.acknowledgementStrategy.generateAcknowledgementMessage(message));
	}
}
