package external.connection.incoming;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collection;

public class StandardReader implements IMessageReadingStrategy {
	
	private InputStream is;
	private BufferedReader r;
	
	public StandardReader(InputStream is) {
		this.is = new DataInputStream(is);
		this.r = new BufferedReader(new InputStreamReader(this.is));
//		try {
//			this.r.mark(Integer.MAX_VALUE);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
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
//		System.out.println("Reading message");
	     try {
//	    	r.reset();
	    	if (r.ready()) {
	    		result = r.readLine();
	    	}
//			r.mark(Integer.MAX_VALUE);
		} catch (IOException e) {
//			e.printStackTrace();
		}
//		System.out.println("Read message: " + result);
	    if (result == null || result.length() == 0 || result.charAt(0) == 0) {
	    	return null;
	    }
		return result;
	}

	@Override
	public String[] readMessages() {
		Collection<String> lines = new ArrayList<String>();
		String currentMessage = this.readMessage();
		while (currentMessage != null && currentMessage != "") {
//			System.out.println("Reading message: " + currentMessage);
			lines.add(currentMessage);
			currentMessage = this.readMessage();
		}
		if (lines.isEmpty()) {
			return null;
		} else {
			return lines.toArray(String[]::new);
		}
	}
}
