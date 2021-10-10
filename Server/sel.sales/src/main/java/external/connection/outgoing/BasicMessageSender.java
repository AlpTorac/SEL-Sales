package external.connection.outgoing;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;

import external.connection.IConnection;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;

public class BasicMessageSender implements IMessageSendingStrategy {
	private IMessageSerialiser messageSerialiser = new MessageSerialiser(new StandardMessageFormat());
	
	@Override
	public boolean sendMessage(IConnection conn, IMessage message) {
		DataOutputStream os = new DataOutputStream(conn.getOutputStream());
		BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
		try {
			if (!conn.isClosed()) {
				System.out.println("Writing: " + messageSerialiser.serialise(message));
				writer.write(messageSerialiser.serialise(message));
				writer.flush();
				System.out.println("Wrote: " + messageSerialiser.serialise(message));
			}
		} catch (IOException e) {
//			e.printStackTrace();
		}
		conn.refreshOutputStream();
		return true;
	}

}
