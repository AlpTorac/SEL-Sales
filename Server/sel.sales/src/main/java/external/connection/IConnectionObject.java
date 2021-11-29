package external.connection;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

public interface IConnectionObject extends Closeable {
	Object getConnectionObject();
	String getTargetAddress();
	InputStream getInputStream();
	OutputStream getOutputStream();
}
