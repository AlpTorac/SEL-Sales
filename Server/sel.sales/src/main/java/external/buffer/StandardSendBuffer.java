package external.buffer;

import java.io.OutputStream;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;

import external.connection.BasicMessageSender;

public class StandardSendBuffer extends SendBuffer {
	public StandardSendBuffer(OutputStream os, ExecutorService es) {
		super(os, new BasicMessageSender(), new FIFOBuffer(), new FixTimeoutStrategy(2000, ChronoUnit.MILLIS), es);
	}
}
