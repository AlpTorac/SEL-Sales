package external.buffer;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;

import external.connection.BasicMessageSender;
import external.connection.IConnection;

public class StandardSendBuffer extends SendBuffer {
	public StandardSendBuffer(IConnection conn, ExecutorService es) {
		super(conn, new BasicMessageSender(), new FIFOBuffer(), new FixTimeoutStrategy(2000, ChronoUnit.MILLIS), es);
	}
}
