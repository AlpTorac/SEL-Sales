package test.external.dummy;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.IDeviceManager;
import external.connection.Service;

public class DummyService extends Service {

	public DummyService(String id, String name, IDeviceManager DeviceManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(id, name, DeviceManager, controller, es, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
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
