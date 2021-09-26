package test.external.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import external.message.MessageFlag;

class MessageFlagTest {
	@Test
	void stringToMessageFlagTest() {
		Assertions.assertEquals(MessageFlag.ACKNOWLEDGEMENT, MessageFlag.stringToMessageFlag("ack"));
		Assertions.assertThrows(IllegalArgumentException.class, () -> {MessageFlag.stringToMessageFlag("asdfkhjgsdf");});
	}
}
