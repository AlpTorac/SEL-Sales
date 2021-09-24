package external.buffer;

import java.io.Closeable;

import external.message.IMessage;

/**
 * The interface implemented by the constructs that send the messages to be sent.
 * <p>
 * Note: Do not send acknowledgements this way. Since these constructs block on sending,
 * acknowledgements should be sent asynchronously and independent of these constructs.
 * @author atora
 */
public interface ISendBuffer extends HasTimeout, Closeable {
	/**
	 * Sends the message in line, if the send buffer is not waiting on an acknowledgement.
	 * @return Whether the message is successfully sent.
	 */
	boolean sendMessage();
	/**
	 * No messages can be sent when this construct is blocked.
	 * @return Whether the send buffer is waiting on an acknowledgement.
	 */
	boolean isBlocked();
	/**
	 * Receive the given acknowledgement message.
	 */
	void receiveAcknowledgement(IMessage message);
	boolean isEmpty();
	void addMessage(IMessage message);
}
