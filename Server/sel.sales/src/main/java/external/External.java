package external;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import controller.IController;
import external.connection.IService;
import external.connection.IServiceConnectionManager;
import external.device.IDeviceManager;
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
	
	protected abstract IDeviceManager initDeviceManager();
	
	protected IServiceConnectionManager getServiceConnectionManager() {
		return this.getService().getServiceConnectionManager();
	}
	
	protected IDeviceManager getDeviceManager() {
		return this.getService().getDeviceManager();
	}
	
	protected abstract IService initService();
	
	@Override
	public void subscribe() {
		this.getModel().subscribe(this);
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
	
//	protected void setService(IService service) {
//		this.service = service;
//		System.out.println("Service set");
//	}
	
	protected void setService(IService service) {
		this.service = service;
		this.getService().publish();
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
			this.getService().receiveKnownDeviceData(this.getModel().getAllKnownDeviceData());
		}
	}
	@Override
	public void refreshSettings() {
		if (this.getService() != null) {
			this.getService().receiveSettings(this.getModel().getSettings());
		}
	}
	@Override
	public void close() {
		this.es.shutdown();
		if (this.getServiceConnectionManager() != null) {
			this.getServiceConnectionManager().close();
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
