package test.external.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import external.message.MessageContext;

class MessageContextTest {
	@Test
	void stringToMessageContext() {
		Assertions.assertEquals(MessageContext.MENU, MessageContext.stringToMessageContext("menu"));
		Assertions.assertEquals(MessageContext.ORDER, MessageContext.stringToMessageContext("order"));
		Assertions.assertNull(MessageContext.stringToMessageContext("asdfkhjgsdf"));
	}
}
