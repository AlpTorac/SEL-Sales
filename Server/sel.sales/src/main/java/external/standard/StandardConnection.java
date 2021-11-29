package external.standard;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import external.connection.IConnection;
import external.connection.IConnectionObject;

public class StandardConnection implements IConnection {

	private String address;
	private InputStream is;
	private OutputStream os;
	private IConnectionObject connectionObject;
	
	public StandardConnection(IConnectionObject connectionObject) {
		this.connectionObject = connectionObject;
		this.address = this.connectionObject.getTargetAddress();
		this.is = this.connectionObject.getInputStream();
		this.os = this.connectionObject.getOutputStream();
	}
	
	@Override
	public void close() throws IOException {
		this.connectionObject.close();
	}

	@Override
	public InputStream getInputStream() {
		return this.is;
	}

	@Override
	public OutputStream getOutputStream() {
		return this.os;
	}

	@Override
	public boolean isClosed() {
		return this.connectionObject == null;
	}

	@Override
	public String getTargetDeviceAddress() {
		return this.address;
	}

	@Override
	public void refreshInputStream() {
//		try {
//			this.is.close();
//			this.is = this.conn.openInputStream();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

	@Override
	public void refreshOutputStream() {
//		try {
//			this.os.close();
//			this.os = this.conn.openOutputStream();
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
	}

}
