package test.external.dummy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import external.connection.IConnection;

public class DummyConnection implements IConnection {

	private InputStream is;
	private OutputStream os;
	private String clientAddress;
	private boolean isClosed = false;
	
	public DummyConnection(String clientAddress) {
		this.clientAddress = clientAddress;
	}
	
	@Override
	public void close() throws IOException {
		this.is.close();
		this.os.close();
		this.isClosed = true;
	}

	@Override
	public InputStream getInputStream() {
		try {
			if (this.is == null || this.is.available() == 0) {
				this.is = new ByteArrayInputStream(new byte[100]);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return this.is;
	}

	@Override
	public OutputStream getOutputStream() {
		if (this.os == null) {
			this.os = new ByteArrayOutputStream();
		}
		return this.os;
	}

	@Override
	public boolean isClosed() {
		return this.isClosed;
	}

	@Override
	public String getTargetClientAddress() {
		return this.clientAddress;
	}

}
