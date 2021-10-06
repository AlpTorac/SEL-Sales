package external.bluetooth;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.bluetooth.RemoteDevice;
import javax.microedition.io.StreamConnection;

import external.connection.IConnection;

public class BluetoothConnection implements IConnection {
	private StreamConnection conn;
	private String targetAddress;
	private InputStream is;
	private OutputStream os;
	
	public BluetoothConnection(StreamConnection conn) {
		this.conn = conn;
		try {
			this.targetAddress = RemoteDevice.getRemoteDevice(conn).getBluetoothAddress();
			this.is = this.conn.openInputStream();
			this.os = this.conn.openOutputStream();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	}
	
	@Override
	public void close() throws IOException {
		this.conn.close();
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
		return this.conn == null;
	}

	@Override
	public String getTargetClientAddress() {
		return this.targetAddress;
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
