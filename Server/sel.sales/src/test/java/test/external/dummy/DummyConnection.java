package test.external.dummy;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

import external.connection.IConnection;
import test.GeneralTestUtilityClass;

public class DummyConnection implements IConnection {

	private ISwithBuffer targetInputBuffer;
	
	private byte[] inputBuffer;
	private byte[] outputBuffer;
	
	private ISwithBuffer is;
	private OSwithBuffer os;
	private String DeviceAddress;
	private boolean isClosed = false;
	
	private long lag = 150;
	
	public DummyConnection(String DeviceAddress) {
		this.DeviceAddress = DeviceAddress;
		inputBuffer = new byte[1000];
		outputBuffer = new byte[1000];
		this.is = new ISwithBuffer(inputBuffer);
		this.os = new OSwithBuffer(outputBuffer);
	}
	
	public void setInputBuffer(byte[] newInputBuffer) {
		this.inputBuffer = newInputBuffer;
		this.is.setBuffer(this.inputBuffer);
	}
	
	public void setOutputBuffer(byte[] newOutputBuffer) {
		this.outputBuffer = newOutputBuffer;
		this.os.setBuffer(this.outputBuffer);
	}
	
	public void setInputTarget(ISwithBuffer targetInputBuffer) {
		this.targetInputBuffer = targetInputBuffer;
	}
	
	public void fillInputBuffer(String string) {
		GeneralTestUtilityClass.performWait(lag);
		this.is.refresh();
		this.is.fillInputBuffer(string);
	}
	
	public long getLag() {
		return this.lag;
	}
	
	public byte[] getOutputStreamBuffer() {
		return this.os.getBuffer();
	}
	
	public void resetInputStream() {
//		this.inputBuffer = new byte[1000];
//		this.is.setBuffer(this.inputBuffer);
	}
	
	public void resetOutputStream() {
//		this.outputBuffer = new byte[1000];
//		this.os.setBuffer(this.outputBuffer);
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
	public ISwithBuffer getInputStream() {
//		if (this.is == null) {
//			this.is = new ByteArrayInputStream(buffer);
//		}
		return this.is;
	}

	@Override
	public OSwithBuffer getOutputStream() {
//		if (this.os == null) {
//			this.os = new ByteArrayOutputStream();
//		}
		return this.os;
	}

	@Override
	public boolean isClosed() {
		return this.isClosed;
	}

	@Override
	public String getTargetDeviceAddress() {
		return this.DeviceAddress;
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

	private int getLastValidBytePos(byte[] buffer) {
		int i = 0;
		while (i < buffer.length && buffer[i] != 0) {
			i++;
		}
		return i;
	}
	
	private String getValidString(String string) {
		int begin = 0;
		while (string.charAt(begin) == 0) {
			begin++;
		}
		int end = begin;
		while (end < string.length() && string.charAt(end) != 0) {
			end++;
		}
		return string.substring(begin, end);
	}
	
	public class OSwithBuffer extends ByteArrayOutputStream {
		private OSwithBuffer(byte[] buffer) {
			super();
			this.buf = buffer;
		}
		
		private void setCount() {
			this.count = getLastValidBytePos(buf);
		}
		
		protected void setBuffer(byte[] newBuffer) {
			this.buf = newBuffer;
			this.setCount();
		}
		
		protected byte[] getBuffer() {
			return this.buf;
		}
		
		protected void refresh() {
			this.setBuffer(new byte[this.buf.length]);
		}
		
		@Override
		public void flush() throws IOException {
			if (targetInputBuffer != null) {
				String content = getValidString(new String(this.buf));
//				System.out.println("Writing to target: " + content);
				targetInputBuffer.fillInputBuffer(content);
//				System.out.println("flushing: " + content);
			}
			this.refresh();
		}
	}
	
	public class ISwithBuffer extends ByteArrayInputStream {
		private ISwithBuffer(byte[] buffer) {
			super(buffer);
			this.setCount();
		}
		
		private void setCount() {
			this.count = getLastValidBytePos(buf);
		}
		
		protected void setBuffer(byte[] newBuffer) {
			this.pos = 0;
			this.mark = 0;
			this.buf = newBuffer;
			this.setCount();
		}
		
		protected void fillInputBuffer(String string) {
			this.refresh();
			byte[] bytes = getValidString(string).getBytes();
			
			int i = this.count;
			for (int j = 0; j < bytes.length; j++) {
				buf[i+j] = bytes[j];
			}
			this.count = i + bytes.length;
		}
		
		@Override
		public synchronized int read() {
			int r = super.read();
//			if (r != -1) {
//				System.out.println("Read: " + r);
//			}
			this.mark(Integer.MAX_VALUE);
			return r;
		}
		
		@Override
		public int read(byte b[], int off, int len) {
	        int r = super.read(b, off, len);
//	        if (r != -1) {
//	        	System.out.println("Read: " + r);
//	        }
	        this.mark(Integer.MAX_VALUE);
	        return r;
	    }
		
		protected void refresh() {
			this.setBuffer(new byte[this.buf.length]);
		}
	}
	
}
