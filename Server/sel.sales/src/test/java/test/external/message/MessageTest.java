package test.external.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
//@Execution(value = ExecutionMode.SAME_THREAD)
class MessageTest {
	private int sequenceNumber;
	private MessageContext context;
	private MessageFlag[] flags;
	private String serialisedData;
	private IMessage message;
	
	@BeforeEach
	void prep() {
		sequenceNumber = 1;
		context = MessageContext.MENU;
		flags = new MessageFlag[] {};
		serialisedData = "menuData";
		message = new Message(sequenceNumber, context, flags, serialisedData);
	}
	
	@Test
	void getSequenceNumberTest() {
		Assertions.assertEquals(sequenceNumber, message.getSequenceNumber());
	}
	
	@Test
	void getMessageContextTest() {
		Assertions.assertEquals(context, message.getMessageContext());
	}
	
	@Test
	void getMessageFlagsTest() {
		Assertions.assertArrayEquals(flags, message.getMessageFlags());
	}
	
	@Test
	void getSerialisedDataTest() {
		Assertions.assertEquals(serialisedData, message.getSerialisedData());
	}
	
	@Test
	void getMinimalAcknowledgementTest() {
		IMessage acknowledgement = message.getMinimalAcknowledgementMessage();
		MessageTestUtilityClass.assertMessageContentEquals(sequenceNumber, context, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, serialisedData, acknowledgement);
	}
	
	@Test
	void acknowledgementOfAcknowledgementTest() {
		Assertions.assertNull(message.getMinimalAcknowledgementMessage().getMinimalAcknowledgementMessage());
	}
	
	@Test
	void hasFlagTest() {
		Assertions.assertFalse(message.hasFlag(MessageFlag.ACKNOWLEDGEMENT));
		IMessage acknowledgement = message.getMinimalAcknowledgementMessage();
		Assertions.assertTrue(acknowledgement.hasFlag(MessageFlag.ACKNOWLEDGEMENT));
	}
	
	@Test
	void hasFlagMultiTest() {
		message = new Message(sequenceNumber, context, new MessageFlag[] {null, MessageFlag.ACKNOWLEDGEMENT, null}, serialisedData);
		Assertions.assertTrue(message.hasFlag(MessageFlag.ACKNOWLEDGEMENT));
		
		message = new Message(sequenceNumber, context, new MessageFlag[] {null, null, null}, serialisedData);
		Assertions.assertFalse(message.hasFlag(MessageFlag.ACKNOWLEDGEMENT));
	}
	
	@Test
	void hasContextTest() {
		Assertions.assertTrue(message.hasContext(context));
		Assertions.assertFalse(message.hasContext(MessageContext.ORDER));
	}
	
	@Test
	void noContextTest() {
		IMessage messageWOContext = new Message(sequenceNumber, null, flags, serialisedData);
		Assertions.assertFalse(messageWOContext.hasContext(context));
	}
	
	@Test
	void cloneAndEqualsTest() {
		IMessage emptyMessage = new Message(null, null, null);
		Assertions.assertFalse(emptyMessage == emptyMessage.clone());
		Assertions.assertTrue(emptyMessage.equals(new Message(null, null, null)));
		
		IMessage messageWithContext = new Message(MessageContext.MENU, null, null);
		Assertions.assertTrue(messageWithContext.clone().hasContext(MessageContext.MENU));
		Assertions.assertTrue(messageWithContext.equals(messageWithContext.clone()));
		
		IMessage flaggedMessage = new Message(null, new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}, null);
		Assertions.assertTrue(flaggedMessage.clone().hasFlag(MessageFlag.ACKNOWLEDGEMENT));
		Assertions.assertTrue(flaggedMessage.equals(flaggedMessage.clone()));
		
		String data = "someData";
		IMessage messageWithData = new Message(null, null, data);
		Assertions.assertTrue(messageWithData.clone().getSerialisedData().equals(data));
		Assertions.assertTrue(messageWithData.equals(messageWithData.clone()));
		
		Assertions.assertFalse(emptyMessage.equals(messageWithContext));
		Assertions.assertFalse(emptyMessage.equals(flaggedMessage));
		Assertions.assertFalse(emptyMessage.equals(messageWithData));
		
		Assertions.assertFalse(messageWithContext.equals(emptyMessage));
		Assertions.assertFalse(messageWithContext.equals(flaggedMessage));
		Assertions.assertFalse(messageWithContext.equals(messageWithData));
		
		Assertions.assertFalse(flaggedMessage.equals(messageWithContext));
		Assertions.assertFalse(flaggedMessage.equals(emptyMessage));
		Assertions.assertFalse(flaggedMessage.equals(messageWithData));
		
		Assertions.assertFalse(messageWithData.equals(messageWithContext));
		Assertions.assertFalse(messageWithData.equals(flaggedMessage));
		Assertions.assertFalse(messageWithData.equals(emptyMessage));
		
		IMessage seqNumMessage1 = new Message(0, null, null, null);
		IMessage seqNumMessage2 = new Message(1, null, null, null);
		
		Assertions.assertTrue(seqNumMessage2.equals(seqNumMessage2.clone()));
		Assertions.assertFalse(seqNumMessage1.equals(seqNumMessage2));
		
		String data2 = "someOtherData";
		IMessage messageWithSomeOtherData = new Message(null, null, data2);
		Assertions.assertFalse(messageWithData.equals(messageWithSomeOtherData));
	}
}
