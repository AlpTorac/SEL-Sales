package test.external.message;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import external.message.MessageContext;
import model.exceptions.NoSuchMessageContextException;
//@Execution(value = ExecutionMode.SAME_THREAD)
class MessageContextTest {
	@Test
	void stringToMessageContext() {
		Assertions.assertEquals(MessageContext.MENU, MessageContext.stringToMessageContext("menu"));
		Assertions.assertEquals(MessageContext.ORDER, MessageContext.stringToMessageContext("order"));
		Assertions.assertThrows(NoSuchMessageContextException.class, ()->{MessageContext.stringToMessageContext("asdfkhjgsdf");});
	}
}
