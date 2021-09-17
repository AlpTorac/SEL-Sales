package external;

import java.io.InputStream;

public interface IMessageReadingStrategy {
	String readMessage(InputStream inputStream);
}
