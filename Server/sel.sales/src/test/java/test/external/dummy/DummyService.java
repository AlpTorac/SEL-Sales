package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.IDeviceManager;
import external.connection.Service;

public class DummyService extends Service {

	public final static long DEFAULT_PP_TIMEOUT = 200;
	public final static long DEFAULT_PP_MINIMAL_TIMEOUT = 100;
	public final static long SEND_TIMEOUT = 2000;
	public final static int RESEND_LIMIT = 10;
	
	public final static long ESTIMATED_PP_TIMEOUT = DEFAULT_PP_TIMEOUT * (RESEND_LIMIT + 1);
	
	public DummyService(String id, String name, IDeviceManager DeviceManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(id, name, DeviceManager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	
	public DummyService(String id, String name, IDeviceManager DeviceManager, IController controller, ExecutorService es) {
		this(id, name, DeviceManager, controller, es, DEFAULT_PP_TIMEOUT, DEFAULT_PP_MINIMAL_TIMEOUT, SEND_TIMEOUT, RESEND_LIMIT);
	}

	@Override
	public void publish() {
		this.scm = new DummyServiceConnectionManager(
				this.getDeviceManager(),
				this.getController(),
				es,
				this.getPingPongTimeout(),
				this.getMinimalPingPongDelay(),
				this.getSendTimeout(),
				this.getResendLimit());
		this.scm.makeNewConnectionThread();
	}

	@Override
	public String generateURL() {
		return "URL of service " + this.getName();
	}

}
