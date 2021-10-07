package external.connection.pingpong;

import java.io.Closeable;

import external.connection.DisconnectionListener;
import external.connection.timeout.HasTimeout;
import external.message.IMessage;

public interface IPingPong extends HasTimeout, Closeable {
	boolean sendPingPongMessage();
	boolean isRunning();
	void receiveResponse(IMessage message);
	int getRemainingResendTries();
	void setDisconnectionListener(DisconnectionListener dl);
	void startTimeoutTimer();
}
