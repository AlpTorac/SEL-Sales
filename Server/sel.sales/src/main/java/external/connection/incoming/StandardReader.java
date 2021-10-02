package external.connection.incoming;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class StandardReader implements IMessageReadingStrategy {
	
	public StandardReader() {}
	
	// Check the first character of the stream to decide, whether it is time to read.
	protected boolean checkReadCondition(BufferedReader r) {
		int readChar = -1;
		try {
			r.mark(1);
			readChar = r.read();
			r.reset();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return readChar > 0;
	}

	/**
	 * Assumes that each line is ended with \n or \r
	 */
	@Override
	public String[] readMessages(InputStream is) {
		String[] result = null;
		BufferedReader r = new BufferedReader(new InputStreamReader(is));
		if (this.checkReadCondition(r)) {
		     result = r.lines().filter(l -> l != "" && l.charAt(0) != 0).toArray(String[]::new);
		}
		return result;
	}
}
