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
	
	private long pingPongTimeout;
	private long minimalPingPongDelay;
	private long sendTimeout;
	private int resendLimit;
	
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
		return service;
	}
	
	protected void setService(IService service) {
		this.service = service;
	}
	@Override
	public void rediscoverClients(Runnable afterDiscoveryAction) {
		this.service.getClientManager().discoverClients(afterDiscoveryAction);
	}
	@Override
	public void refreshKnownClients() {
		this.service.receiveKnownClientData(this.model.getAllKnownClientData());
	}
	@Override
	public void refreshSettings() {
		this.service.receiveSettings(this.model.getSettings());
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
