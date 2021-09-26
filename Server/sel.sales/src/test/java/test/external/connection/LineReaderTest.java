package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import external.connection.IMessageReadingStrategy;
import external.connection.LineReader;

class LineReaderTest {
	private IMessageReadingStrategy mrs;
	private InputStream is;
	private byte[] buffer;
	private int bufferLen;
	
	@BeforeEach
	void prep() {
		bufferLen = 100;
		buffer = new byte[bufferLen];
		is = new ByteArrayInputStream(buffer);
		mrs = new LineReader(is);
	}
	
	@AfterEach
	void cleanUp() {
		try {
			is.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void fillBuffer(String bufferContent) {
		byte[] bytes = bufferContent.getBytes();
		int i = 0;
		byte currentByte = buffer[i];
		while (currentByte != 0) {
			i++;
			currentByte = buffer[i];
		}
		for (int j = 0; j < bytes.length; j++) {
			buffer[i+j] = bytes[j];
		}
	}
	
	@Test
	void readSingleLineTest() {
		String bc = "abcdefg";
		String bufferContent = bc + "\n";
		this.fillBuffer(bufferContent);
		Assertions.assertEquals(bc, mrs.readMessage());
	}
	
	@Test
	void readMultipleLinesTest() {
		String part1 = "abcdefg";
		String part2 = "gdfshij";
		String bufferContent = part1 + "\n" + part2 + "\n";
		this.fillBuffer(bufferContent);
		Assertions.assertEquals(part1, mrs.readMessage());
		Assertions.assertEquals(part2, mrs.readMessage());
	}
	
}
