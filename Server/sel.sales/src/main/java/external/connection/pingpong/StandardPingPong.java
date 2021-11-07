package external.connection.pingpong;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.outgoing.BasicMessageSender;
import external.connection.timeout.PingPongTimeoutStrategy;

public class StandardPingPong extends PingPong {
	public StandardPingPong(IConnection conn, ExecutorService es) {
		super(conn, new BasicMessageSender(), new PingPongTimeoutStrategy(10000, ChronoUnit.MILLIS, es, 1000), 10);
	}
	public StandardPingPong(IConnection conn, ExecutorService es, long minimalDelay, int resendLimit, long pingPongTimeoutInMillis) {
		super(conn, new BasicMessageSender(), new PingPongTimeoutStrategy(pingPongTimeoutInMillis, ChronoUnit.MILLIS, es, minimalDelay), resendLimit);
	}
}
