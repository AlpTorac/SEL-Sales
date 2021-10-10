package test.external.message;

import org.junit.jupiter.api.Assertions;

import external.message.IMessage;
import external.message.Message;
import external.message.MessageContext;
import external.message.MessageFlag;
import test.GeneralTestUtilityClass;

public final class MessageTestUtilityClass {
	public static void assertMessageContentEquals(int sequenceNumber, MessageContext context, MessageFlag[] flags, String serialisedData, IMessage parsedMessage) {
		Assertions.assertEquals(sequenceNumber, parsedMessage.getSequenceNumber());
		Assertions.assertEquals(context, parsedMessage.getMessageContext());
		Assertions.assertArrayEquals(flags, parsedMessage.getMessageFlags());
		Assertions.assertEquals(serialisedData, parsedMessage.getSerialisedData());
	}
	
	public static void assertMessageEquals(IMessage expected, IMessage actual) {
		assertMessageContentEquals(actual.getSequenceNumber(), actual.getMessageContext(),
				actual.getMessageFlags(), actual.getSerialisedData(), expected);
	}
	
	public static IMessage generateRandomMessage(int maximumSeqNumber, int maximumTextLength) {
		int sequenceNumber = 0;
		MessageContext context = null;
		MessageFlag[] flags = null;
		
		sequenceNumber = GeneralTestUtilityClass.generateRandomNumber(0, maximumSeqNumber);
		
		switch (GeneralTestUtilityClass.generateRandomNumber(0, MessageContext.values().length)) {
		case 0: context = MessageContext.MENU; break;
		case 1: context = MessageContext.ORDER; break;
		case 2: context = MessageContext.PINGPONG; break;
		default: context = null; break;
		}
		switch (GeneralTestUtilityClass.generateRandomNumber(0, MessageFlag.values().length + 1)) {
		case 0: flags = new MessageFlag[] {}; break;
		case 1: flags = new MessageFlag[] {MessageFlag.ACKNOWLEDGEMENT}; break;
		default: flags = null; break;
		}
		return new Message(sequenceNumber, context, flags, GeneralTestUtilityClass.generateRandomString(GeneralTestUtilityClass.generateRandomNumber(0, maximumTextLength)));
	}
}
