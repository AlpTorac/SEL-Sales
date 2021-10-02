package external.connection.incoming;

import java.io.InputStream;

public interface IMessageReadingStrategy {
	String[] readMessages(InputStream is);
}
