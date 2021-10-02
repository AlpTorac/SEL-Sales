package external.connection.incoming;

import java.io.Closeable;

public interface IMessageReceptionist extends Closeable {
	boolean checkForMessages();
}
