package test.external.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.message.MessageFlag;
@Execution(value = ExecutionMode.SAME_THREAD)
class MessageFlagTest {
	@Test
	void stringToMessageFlagTest() {
		Assertions.assertEquals(MessageFlag.ACKNOWLEDGEMENT, MessageFlag.stringToMessageFlag("ack"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> {MessageFlag.stringToMessageFlag("asdfkhjgsdf");});
	}
}
