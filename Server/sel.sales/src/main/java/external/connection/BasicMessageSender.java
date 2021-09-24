package external.connection;

import java.io.IOException;
import java.io.OutputStream;

import external.message.IMessage;

public class BasicMessageSender implements IMessageSendingStrategy {

	@Override
	public boolean sendMessage(OutputStream os, IMessage message) {
		try {
			os.write(message.toString().getBytes());
			os.flush();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
