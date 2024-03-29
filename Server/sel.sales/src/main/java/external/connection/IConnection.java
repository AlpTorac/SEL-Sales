package external.connection;

import java.io.Closeable;
import java.io.InputStream;
import java.io.OutputStream;

public interface IConnection extends Closeable {
	InputStream getInputStream();
	OutputStream getOutputStream();
	boolean isClosed();
	String getTargetDeviceAddress();
	void refreshInputStream();
	void refreshOutputStream();
}
