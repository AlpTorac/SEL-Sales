package test.external.dummy;

import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.outgoing.IMessageSendingStrategy;
import external.connection.pingpong.PingPong;
import external.connection.timeout.ITimeoutStrategy;

public class DummyPingPong extends PingPong {
	public DummyPingPong(IConnection conn, IMessageSendingStrategy mss, ITimeoutStrategy ts, ExecutorService es,
			int resendLimit) {
		super(conn, mss, ts, es, resendLimit);
	}
}
