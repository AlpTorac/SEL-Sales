package external.connection;

import java.io.InputStream;

public interface IMessageReadingStrategy {
	String[] readMessages(InputStream is);
}
