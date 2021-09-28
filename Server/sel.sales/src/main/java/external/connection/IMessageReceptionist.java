package external.connection;

import java.io.Closeable;

public interface IMessageReceptionist extends Closeable {
	boolean checkForMessages();
}
