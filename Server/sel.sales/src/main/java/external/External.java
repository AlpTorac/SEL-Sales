package external;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import controller.IController;
import external.connection.IService;
import external.connection.ServiceConnectionManager;
import external.device.IDeviceManager;
import model.IModel;
import model.settings.ISettings;

public abstract class External implements IExternal {
	
	private ExecutorService es;
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
		this.es = Executors.newCachedThreadPool();
		this.pingPongTimeout = pingPongTimeout;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
		this.subscribe();
	}
	
	protected ExecutorService getES() {
		return this.es;
	}
	
	protected void setupService() {
		this.setService(this.initService());
	}
	
	protected abstract IDeviceManager initDeviceManager();
	
	protected ServiceConnectionManager getServiceConnectionManager() {
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
		if (this.service == null) {
			this.setupService();
		}
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
			this.getDeviceManager().discoverDevices(afterDiscoveryAction);
//			this.getService().getDeviceManager().discoverDevices(afterDiscoveryAction);
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
//		if (this.getService() != null) {
//			this.getService().receiveSettings(this.getModel().getSettings());
//		}
		this.notifyInnerConstructs(this.getModel().getSettings());
	}
	@Override
	public void close() {
		this.es.shutdown();
		if (this.getServiceConnectionManager() != null) {
			this.getServiceConnectionManager().close();
		}
	}
	
	@Override
	public void notifyInnerConstructs(ISettings settings) {
		if (this.getService() != null) {
			this.getService().receiveSettings(settings);
		}
		if (this.getServiceConnectionManager() != null) {
			this.getServiceConnectionManager().notifyInnerConstructs(settings);
		}
	}

	@Override
	public void setMinimalPingPongDelay(long minimalPingPongDelay) {
		this.minimalPingPongDelay = minimalPingPongDelay;
	}

	@Override
	public void setSendTimeoutInMillis(long sendTimeoutInMillis) {
		this.sendTimeout = sendTimeoutInMillis;
	}

	@Override
	public void setPingPongTimeoutInMillis(long pingPongTimeoutInMillis) {
		this.pingPongTimeout = pingPongTimeoutInMillis;
	}

	@Override
	public void setPingPongResendLimit(int pingPongResendLimit) {
		this.resendLimit = pingPongResendLimit;
	}

	@Override
	public long getSendTimeoutInMillis() {
		return this.sendTimeout;
	}

	@Override
	public long getPingPongTimeoutInMillis() {
		return this.pingPongTimeout;
	}

	@Override
	public int getPingPongResendLimit() {
		return this.resendLimit;
	}
	@Override
	public long getMinimalPingPongDelay() {
		return this.minimalPingPongDelay;
	}
}
