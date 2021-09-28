package test.external.message;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.message.MessageContext;
@Execution(value = ExecutionMode.SAME_THREAD)
class MessageContextTest {
	@Test
	void stringToMessageContext() {
		Assertions.assertEquals(MessageContext.MENU, MessageContext.stringToMessageContext("menu"));
		Assertions.assertEquals(MessageContext.ORDER, MessageContext.stringToMessageContext("order"));
		Assertions.assertNull(MessageContext.stringToMessageContext("asdfkhjgsdf"));
	}
}
