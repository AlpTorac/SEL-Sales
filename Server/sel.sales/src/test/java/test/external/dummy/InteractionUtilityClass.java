package test.external.dummy;

public class InteractionUtilityClass {
	public static void bindConnectionStreams(DummyConnection conn1, DummyConnection conn2) {
		conn1.setInputTarget(conn2.getInputStream());
		conn2.setInputTarget(conn1.getInputStream());
	}
}
