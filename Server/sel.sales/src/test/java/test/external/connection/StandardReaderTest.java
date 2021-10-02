package test.external.connection;

import static org.junit.jupiter.api.Assertions.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.connection.incoming.IMessageReadingStrategy;
import external.connection.incoming.IMessageReceptionist;
import external.connection.incoming.StandardReader;
import test.GeneralTestUtilityClass;
import test.external.buffer.BufferUtilityClass;
import test.external.dummy.DummyConnection;
@Execution(value = ExecutionMode.SAME_THREAD)
class StandardReaderTest {
	private long waitTime = 300;
	
	private IMessageReadingStrategy mrs;
	private DummyConnection conn;
	private boolean isNotified = false;
	
	@BeforeEach
	void prep() {
		conn = new DummyConnection("clientaddress");
		mrs = new StandardReader();
		isNotified = false;
	}
	
	@AfterEach
	void cleanUp() {
		try {
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		isNotified = false;
	}
	
	@Test
	void readSingleLineTest() {
		String bc = "abcdefg";
		String bufferContent = bc + "\n";
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), bufferContent);
		String[] sm = mrs.readMessages(conn.getInputStream());
		Assertions.assertEquals(bc, sm[0]);
	}
	
	@Test
	void readMultipleLinesTest() {
		String part1 = "abcdefg";
		String part2 = "gdfshij";
		String bufferContent = part1 + "\n" + part2 + "\n";
		BufferUtilityClass.fillBuffer(conn.getInputStreamBuffer(), bufferContent);
		String[] sm = mrs.readMessages(conn.getInputStream());
		Assertions.assertEquals(part1, sm[0]);
		Assertions.assertEquals(part2, sm[1]);
	}
}
