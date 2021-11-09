package external.connection;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.device.DeviceDiscoveryListener;
import external.device.IDeviceManager;
import model.connectivity.IDeviceData;
import model.settings.ISettings;

public abstract class Service implements IService {
	protected ExecutorService es;
	protected IServiceConnectionManager scm;
	private IDeviceManager deviceManager;
	
	private String id;
	private String url;
	private String name;
	
	private IController controller;
	
	private volatile long pingPongTimeout;
	private volatile long minimalPingPongDelay;
	private volatile long sendTimeout;
	private volatile int resendLimit;
	
	public Service(String id, String name, IDeviceManager deviceManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		this.id = id;
		this.name = name;
		this.es = es;
		this.deviceManager = deviceManager;
		this.controller = controller;
		this.url = this.generateURL();
		this.pingPongTimeout = pingPongTimeout;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
		this.getDeviceManager().setDeviceDiscoveryListener(new DeviceDiscoveryListener(this.getController()));
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	@Override
	public String getID() {
		return this.id;
	}

	@Override
	public String getName() {
		return this.name;
	}

	@Override
	public String getURL() {
		return this.url;
	}

	@Override
	public IServiceConnectionManager getServiceConnectionManager() {
		return this.scm;
	}
	
	@Override
	public IDeviceManager getDeviceManager() {
		return this.deviceManager;
	}
	
	@Override
	public void receiveSettings(ISettings settings) {
		if (this.scm != null) {
			this.scm.receiveSettings(settings);
		}
	}
	
	@Override
	public void receiveKnownDeviceData(IDeviceData[] deviceData) {
		if (this.deviceManager != null) {
			this.getDeviceManager().receiveKnownDeviceData(deviceData);
		}
		if (this.scm != null) {
			this.getServiceConnectionManager().receiveKnownDeviceData(deviceData);
		}
	}
	@Override
	public long getMinimalPingPongDelay() {
		return minimalPingPongDelay;
	}
	@Override
	public int getResendLimit() {
		return resendLimit;
	}
	@Override
	public long getPingPongTimeout() {
		return pingPongTimeout;
	}
	@Override
	public long getSendTimeout() {
		return sendTimeout;
	}
}
