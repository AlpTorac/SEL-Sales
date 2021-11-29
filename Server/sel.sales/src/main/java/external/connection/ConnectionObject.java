package external.connection;

import java.io.InputStream;
import java.io.OutputStream;

import external.IConnectionUtility;

public class ConnectionObject implements IConnectionObject {
	private Object connectionObject;
	private IConnectionUtility connUtil;
	
	public ConnectionObject(IConnectionUtility connUtil, Object connectionObject) {
		this.connectionObject = connectionObject;
		this.connUtil = connUtil;
	}
	
	@Override
	public void close() {
		this.connUtil.closeConnectionObject(this);
	}

	@Override
	public String getTargetAddress() {
		return this.connUtil.getConnectionTargetAddress(this);
	}

	@Override
	public InputStream getInputStream() {
		return this.connUtil.openInputStream(this);
	}

	@Override
	public OutputStream getOutputStream() {
		return this.connUtil.openOutputStream(this);
	}

	@Override
	public Object getConnectionObject() {
		return this.connectionObject;
	}

}
