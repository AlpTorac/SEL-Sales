package external.connection.pingpong;

import java.time.temporal.ChronoUnit;
import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.outgoing.BasicMessageSender;
import external.connection.timeout.FixTimeoutStrategy;

public class StandardPingPong extends PingPong {
	public StandardPingPong(IConnection conn, ExecutorService es) {
		super(conn, new BasicMessageSender(), new FixTimeoutStrategy(1000, ChronoUnit.MILLIS, es), es, 10);
	}
	public StandardPingPong(IConnection conn, ExecutorService es, int resendLimit, long pingPongTimeoutInMillis) {
		super(conn, new BasicMessageSender(), new FixTimeoutStrategy(pingPongTimeoutInMillis, ChronoUnit.MILLIS, es), es, resendLimit);
	}
}
