package external.connection.incoming;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StandardReader implements IMessageReadingStrategy {
	
	private InputStream is;
	private BufferedReader r;
	
	public StandardReader(InputStream is) {
		this.is = new DataInputStream(is);
		this.r = new BufferedReader(new InputStreamReader(this.is));
	}
	
	// Check the first character of the stream to decide, whether it is time to read.
	protected boolean checkReadCondition() {
		int readChar = -1;
		try {
			r.mark(1);
			readChar = r.read();
			r.reset();
		} catch (IOException e) {
//			e.printStackTrace();
			return false;
		}
		return readChar > 0;
	}

	/**
	 * Assumes that each line is ended with \n or \r
	 */
	@Override
	public String readMessage() {
		String result = null;
	     try {
			result = r.readLine();
		} catch (IOException e) {
//			e.printStackTrace();
		}
	    if (result == null || result.charAt(0) == 0) {
	    	return null;
	    }
		return result;
	}
}
