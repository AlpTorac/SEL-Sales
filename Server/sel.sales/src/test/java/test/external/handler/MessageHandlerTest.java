package test.external.handler;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.handler.MessageHandler;
import external.message.IMessage;
import external.message.MessageParser;
import external.message.StandardMessageParser;

class MessageHandlerTest {

	private MessageHandler mh;
	
	@BeforeEach
	void setUp() {
		mh = new MessageHandler(new StandardMessageParser()) {
			@Override
			public boolean verify(IMessage message) {
				return true;
			}

			@Override
			public boolean performNeededAction(IMessage message) {
				return true;
			}
		};
	}

	@AfterEach
	void cleanUp() {
	}

	@Test
	void wrongMessageFormatTest() {
		Assertions.assertFalse(mh.handleMessage("fghkfgkhgfsdakgfsdakghjh"));
	}

}
