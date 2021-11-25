package test.external.dummy;

import java.io.Closeable;

import external.connection.IConnection;
import external.device.DeviceDiscoveryStrategy;

public interface IDummyExternal extends Closeable {
	public final static long DEFAULT_PP_TIMEOUT = 2000;
	public final static long DEFAULT_PP_MINIMAL_TIMEOUT = 1000;
	public final static long SEND_TIMEOUT = 2000;
	public final static int RESEND_LIMIT = 3;
	
	public final static long ESTIMATED_PP_TIMEOUT = DEFAULT_PP_TIMEOUT * (RESEND_LIMIT + 1);
	
	IConnection getConnection(String deviceAddress);
	default long getEstimatedPPTimeout() {
		if (this.getPingPongTimeout() == 0 || this.getResendLimit() == 0) {
			return ESTIMATED_PP_TIMEOUT;
		}
		return this.getPingPongTimeout() * (this.getResendLimit() + 1);
	}
	long getPingPongTimeout();
	int getResendLimit();
	void setDiscoveryStrategy(DeviceDiscoveryStrategy cds);
	void close();
}
