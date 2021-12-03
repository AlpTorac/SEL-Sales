package test.external.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import external.message.MessageFlag;
import model.exceptions.NoSuchMessageFlagException;
//@Execution(value = ExecutionMode.SAME_THREAD)
class MessageFlagTest {
	@Test
	void stringToMessageFlagTest() {
		Assertions.assertEquals(MessageFlag.ACKNOWLEDGEMENT, MessageFlag.stringToMessageFlag("ack"));
		Assertions.assertThrows(NoSuchMessageFlagException.class, () -> {MessageFlag.stringToMessageFlag("asdfkhjgsdf");});
	}
}
