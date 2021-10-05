package external.connection.outgoing;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.timeout.FixTimeoutStrategy;

public class StandardSendBuffer extends SendBuffer {
	public StandardSendBuffer(IConnection conn, ExecutorService es) {
		super(conn, new BasicMessageSender(), new FIFOBuffer(), new FixTimeoutStrategy(2000, ChronoUnit.MILLIS), es);
	}
	
	public StandardSendBuffer(IConnection conn, ExecutorService es, long timeoutInMillis) {
		super(conn, new BasicMessageSender(), new FIFOBuffer(), new FixTimeoutStrategy(timeoutInMillis, ChronoUnit.MILLIS), es);
	}
}
