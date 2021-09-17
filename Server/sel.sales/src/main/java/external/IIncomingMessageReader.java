package external;

import java.util.Collection;

public interface IIncomingMessageReader extends Runnable {
	Collection<IMessageHandler> initMessageHandlers();
	boolean handleMessage(String message);
	IConnection getConnection();
	void setMessageReadingStrategy(IMessageReadingStrategy mrs);
	IMessageReadingStrategy getMessageReadingStrategy();
	default void checkStream() {
		String message = "";
		
		while(true) {
			if (this.getConnection().isClosed()) {
				return;
			}
			message = this.getMessageReadingStrategy().readMessage(this.getConnection().getInputStream());
			if (!message.equals("")) {
				this.handleMessage(message);
				message = "";
			}
		}
	}
	
	default void run() {
		this.checkStream();
	}
}
