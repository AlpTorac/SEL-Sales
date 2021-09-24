package external.connection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class LineReader implements IMessageReadingStrategy {
	/**
	 * Assumes that each line is ended with \n or \r
	 */
	@Override
	public String readMessage(InputStream inputStream) {
		BufferedReader r = new BufferedReader(new InputStreamReader(inputStream));
		String message = "";
		try {
			message = r.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return message;
	}
}
