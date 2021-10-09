package test.external.dummy;

import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.outgoing.IMessageSendingStrategy;
import external.connection.pingpong.PingPong;
import external.connection.pingpong.StandardPingPong;
import external.connection.timeout.ITimeoutStrategy;

public class DummyPingPong extends StandardPingPong {
	public DummyPingPong(IConnection conn, ExecutorService es, long minimalDelay, int resendLimit, long pingPongTimeout) {
		super(conn, es, minimalDelay, resendLimit, pingPongTimeout);
	}
}
