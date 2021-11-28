package test.external.connection;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;

import external.connection.incoming.IMessageReadingStrategy;
import external.connection.incoming.StandardReader;
import external.message.IMessage;
import external.message.IMessageSerialiser;
import external.message.MessageSerialiser;
import external.message.StandardMessageFormat;
import test.GeneralTestUtilityClass;
import test.external.dummy.DummyConnection;
import test.external.message.MessageTestUtilityClass;
//@Execution(value = ExecutionMode.SAME_THREAD)
class StandardReaderTest {
	private volatile boolean continueCycle = true;
	private ExecutorService es;
	private long waitTime = 300;
	
	private int minimumNonBlockingWaitTime = 3000;
	private int maximumNonBlockingWaitTime = 5000;
	private int maximumNonBlockingSequenceNumber = 100;
	private int maximumNonBlockingTextLength = 100;
	
	private IMessageSerialiser serialiser = new MessageSerialiser(new StandardMessageFormat());
	private IMessageReadingStrategy mrs;
	private DummyConnection conn;
	private boolean isNotified = false;
	
	private boolean continueCycle() {
		return this.continueCycle;
	}
	
	@BeforeEach
	void prep() {
		es = Executors.newCachedThreadPool();
		conn = new DummyConnection("Deviceaddress");
		mrs = new StandardReader(conn.getInputStream());
		isNotified = false;
		continueCycle = true;
	}
	
	@AfterEach
	void cleanUp() {
		try {
			conn.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			es.awaitTermination(waitTime, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		isNotified = false;
		continueCycle = true;
	}
	
	@Test
	void readSingleLineTest() {
		String bc = "abcdefg";
		String bufferContent = bc + "\n";
		conn.fillInputBuffer(bufferContent);
		String sm = mrs.readMessage();
		Assertions.assertEquals(bc, sm);
	}
	
	@Test
	void readMultipleLinesTest() {
		String part1 = "abcdefg";
		String part2 = "gdfshij";
		String bufferContent = part1 + "\n" + part2 + "\n";
		conn.fillInputBuffer(bufferContent);
		String[] sms = new String[2];
		String sm1 = mrs.readMessage();
		String sm2 = mrs.readMessage();
		System.out.println(sm1 + sm2);
		sms[0] = sm1;
		sms[1] = sm2;
		Assertions.assertEquals(part1, sms[0]);
		Assertions.assertEquals(part2, sms[1]);
	}
	
	@Test
	void readMessagesTest() {
		String part1 = "abcdefg";
		String part2 = "gdfshij";
		String bufferContent = part1 + "\n" + part2 + "\n";
		conn.fillInputBuffer(bufferContent);
		String[] sms = mrs.readMessages();
		Assertions.assertEquals(2, sms.length);
		System.out.println(sms[0] + sms[1]);
		Assertions.assertEquals(part1, sms[0]);
		Assertions.assertEquals(part2, sms[1]);
	}
	
	@Test
	void readMessagesMultipleTimesTest() {
		String part1 = "abcdefg";
		String part2 = "gdfshij";
		String bufferContent = part1 + "\n" + part2 + "\n";
		conn.fillInputBuffer(bufferContent);
		String[] sms = mrs.readMessages();
		Assertions.assertEquals(2, sms.length);
		System.out.println(sms[0] + sms[1]);
		Assertions.assertEquals(part1, sms[0]);
		Assertions.assertEquals(part2, sms[1]);
		
		String part3 = "abcdefg";
		String part4 = "gdfshij";
		String part5 = "fjhjhsdffsd";
		String bufferContent2 = part3 + "\n" + part4 + "\n" + part5 + "\n";
		conn.fillInputBuffer(bufferContent2);
		String[] sms2 = mrs.readMessages();
		Assertions.assertEquals(3, sms2.length);
		System.out.println(sms2[0] + sms2[1] + sms2[2]);
		Assertions.assertEquals(part3, sms2[0]);
		Assertions.assertEquals(part4, sms2[1]);
		Assertions.assertEquals(part5, sms2[2]);
	}
	
	@Test
	void nonBlockingReadTest() {
		final Object lock = new Object();
		ArrayList<String> sentMessages = new ArrayList<String>();
		ArrayList<String> readMessages = new ArrayList<String>();
		
		/*
		 * 3 Runnables run concurrently:
		 * 1) Fills the input stream of conn
		 * 2) Checks for messages to read
		 * 3) Stops the cycles within other runnables after waiting
		 */
		
		// add random messages to the input stream of conn
		es.submit(() -> {
			IMessage m = null;
			String sm = null;
			while (continueCycle()) {
				m = MessageTestUtilityClass.generateRandomMessage(maximumNonBlockingSequenceNumber, maximumNonBlockingTextLength);
				sm = serialiser.serialise(m);
				conn.fillInputBuffer(sm);
				sentMessages.add(sm.replaceAll(System.lineSeparator(), "")); // remove the new line
			}
		});
		
		// Check for messages to read and count how many loops it took
		Future<Integer> cycleCount = es.submit(() -> {
			int cc = 0;
			String[] messages = null;
			while (continueCycle()) {
				if ((messages = mrs.readMessages()) != null) {
					for (String m : messages) {
						readMessages.add(m);
					}
				}
				cc++;
			}
			return cc;
		});
		
		// Wait for a random amount of time and break the cycles
		es.submit(() -> {
			synchronized (lock) {
				try {
					lock.wait(GeneralTestUtilityClass.generateRandomNumber(minimumNonBlockingWaitTime, maximumNonBlockingWaitTime));
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continueCycle = false;
			}
		});
		
		// Wait till the runnables stop
		while (!cycleCount.isDone()) {
			
		}
		
//		Assertions.assertTrue(readMessages.containsAll(sentMessages), "read message count: " + readMessages.size() + ", sent message count: " + sentMessages.size());
//		System.out.println("A total of " + sentMessages.size() + " messages were sent and " + readMessages.size() + " were read and equal to sent ones");
		
		int cycleC = 0;
		try {
			cycleC = cycleCount.get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Assertions.assertTrue(cycleC > Math.max(sentMessages.size(), readMessages.size()), "Cycle Count: " + cycleC + " ,sent message count: " + sentMessages.size());
		System.out.println("In a total of " + cycleC + " cycles, " + readMessages.size() + " messages were read");
//		readMessages.forEach(message -> System.out.println(message));
	}
}
