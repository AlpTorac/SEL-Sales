package external;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.IController;
import external.connection.IService;
import model.IModel;

public abstract class External implements IExternal {
	
	protected ExecutorService es = Executors.newCachedThreadPool();
	private IService service;
	private IModel model;
	private IController controller;
	
	private volatile long pingPongTimeout;
	private volatile long minimalPingPongDelay;
	private volatile long sendTimeout;
	private volatile int resendLimit;
	
	protected External(IController controller, IModel model,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		this.controller = controller;
		this.model = model;
		this.pingPongTimeout = pingPongTimeout;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
		this.subscribe();
	}
	
	protected abstract IService initService();
	
	@Override
	public void subscribe() {
		this.model.subscribe(this);
	}
	
	protected IController getController() {
		return this.controller;
	}
	
	protected IModel getModel() {
		return this.model;
	}

	protected IService getService() {
		return this.service;
	}
	
	protected void setService(IService service) {
		this.service = service;
		System.out.println("Service set");
	}
	@Override
	public void rediscoverDevices(Runnable afterDiscoveryAction) {
		if (this.getService() != null) {
			this.getService().getDeviceManager().discoverDevices(afterDiscoveryAction);
		}
	}
	@Override
	public void refreshKnownDevices() {
		if (this.getService() != null) {
			this.getService().receiveKnownDeviceData(this.model.getAllKnownDeviceData());
		}
	}
	@Override
	public void refreshSettings() {
		if (this.getService() != null) {
			this.getService().receiveSettings(this.model.getSettings());
		}
	}
	@Override
	public void close() {
		this.es.shutdown();
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
