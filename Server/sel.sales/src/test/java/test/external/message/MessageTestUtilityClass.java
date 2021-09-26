package test.external.message;

import org.junit.jupiter.api.Assertions;

import external.message.IMessage;
import external.message.MessageContext;
import external.message.MessageFlag;

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
}
