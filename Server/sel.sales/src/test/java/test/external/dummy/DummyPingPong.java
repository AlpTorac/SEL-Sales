package test.external.dummy;

import java.util.concurrent.ExecutorService;

import external.connection.IConnection;
import external.connection.pingpong.StandardPingPong;

public class DummyPingPong extends StandardPingPong {
	public DummyPingPong(IConnection conn, ExecutorService es, long minimalDelay, int resendLimit, long pingPongTimeout) {
		super(conn, es, minimalDelay, resendLimit, pingPongTimeout);
	}
}
