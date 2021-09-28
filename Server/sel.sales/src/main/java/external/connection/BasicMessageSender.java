package external.connection;

import java.io.IOException;
import java.io.OutputStream;

import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;

public class BasicMessageSender implements IMessageSendingStrategy {
	private IMessageSerialiser messageSerialiser = new MessageSerialiser(new StandardMessageFormat());
	
	@Override
	public boolean sendMessage(IConnection conn, IMessage message) {
		OutputStream os = conn.getOutputStream();
		try {
			os.write(messageSerialiser.serialise(message).getBytes());
			os.flush();
			conn.refreshOutputStream();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
