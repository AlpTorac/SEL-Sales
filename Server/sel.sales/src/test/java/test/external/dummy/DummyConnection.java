package test.external.dummy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import external.connection.IConnection;

public class DummyConnection implements IConnection {

	private byte[] buffer;
	private ByteArrayInputStream is;
	private ByteArrayOutputStream os;
	private String clientAddress;
	private boolean isClosed = false;
	
	public DummyConnection(String clientAddress) {
		this.clientAddress = clientAddress;
		buffer = new byte[100];
		this.is = new ByteArrayInputStream(buffer);
		this.os = new ByteArrayOutputStream();
	}
	
	public byte[] getInputStreamBuffer() {
		return this.buffer;
	}
	
	@Override
	public void close() throws IOException {
		if (this.is != null) {
			this.is.close();
		}
		if (this.os != null) {
			this.os.close();
		}
		this.isClosed = true;
	}

	@Override
	public ByteArrayInputStream getInputStream() {
		if (this.is == null) {
			this.is = new ByteArrayInputStream(buffer);
		}
		return this.is;
	}

	@Override
	public ByteArrayOutputStream getOutputStream() {
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

	@Override
	public void refreshInputStream() {
//		try {
//			this.is.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		buffer = new byte[100];
//		this.is = new ByteArrayInputStream(buffer);
	}

	@Override
	public void refreshOutputStream() {
//		try {
//			this.os.close();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		this.os = new ByteArrayOutputStream();
	}

}
