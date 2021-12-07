package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.IDeviceManager;
import external.connection.Service;

public class DummyService extends Service {
	private boolean attemptToReconnect = false;
	
	public DummyService(String id, String name, IDeviceManager DeviceManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(id, name, DeviceManager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	
	public DummyService(String id, String name, IDeviceManager DeviceManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit, boolean attemptToReconnect) {
		this(id, name, DeviceManager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.attemptToReconnect = attemptToReconnect;
	}
	
	public DummyService(String id, String name, IDeviceManager DeviceManager, IController controller, ExecutorService es) {
		this(id, name, DeviceManager, controller, es,
				DummyConnectionSettings.DEFAULT_PP_TIMEOUT,
				DummyConnectionSettings.DEFAULT_PP_MINIMAL_TIMEOUT,
				DummyConnectionSettings.SEND_TIMEOUT,
				DummyConnectionSettings.RESEND_LIMIT);
	}
	
	public DummyService(String id, String name, IDeviceManager DeviceManager, IController controller, ExecutorService es, boolean attemptToReconnect) {
		this(id, name, DeviceManager, controller, es,
				DummyConnectionSettings.DEFAULT_PP_TIMEOUT,
				DummyConnectionSettings.DEFAULT_PP_MINIMAL_TIMEOUT,
				DummyConnectionSettings.SEND_TIMEOUT,
				DummyConnectionSettings.RESEND_LIMIT);
		this.attemptToReconnect = attemptToReconnect;
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
				this.getResendLimit(),
				this.attemptToReconnect);
		this.scm.makeNewConnectionThread();
	}

	@Override
	public String generateURL() {
		return "URL of service " + this.getName();
	}

}
