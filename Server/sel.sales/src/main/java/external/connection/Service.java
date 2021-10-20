package external.connection;

import java.util.concurrent.ExecutorService;

import controller.IController;
import external.client.ClientDiscoveryListener;
import external.client.IClientManager;
import model.connectivity.IClientData;

public abstract class Service implements IService {
	protected ExecutorService es;
	protected IServiceConnectionManager scm;
	private IClientManager clientManager;
	
	private String id;
	private String url;
	private String name;
	
	private IController controller;
	
	private long pingPongTimeout;
	private long minimalPingPongDelay;
	private long sendTimeout;
	private int resendLimit;
	
	public Service(String id, String name, IClientManager clientManager, IController controller, ExecutorService es,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		this.id = id;
		this.name = name;
		this.es = es;
		this.clientManager = clientManager;
		this.controller = controller;
		this.url = this.generateURL();
		this.pingPongTimeout = pingPongTimeout;
		this.minimalPingPongDelay = minimalPingPongDelay;
		this.sendTimeout = sendTimeout;
		this.resendLimit = resendLimit;
		this.getClientManager().setClientDiscoveryListener(new ClientDiscoveryListener(this.getController()));
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
	public IClientManager getClientManager() {
		return this.clientManager;
	}

	@Override
	public void receiveKnownClientData(IClientData[] clientData) {
		if (this.clientManager != null) {
			this.getClientManager().receiveKnownClientData(clientData);
		}
		if (this.scm != null) {
			this.getServiceConnectionManager().receiveKnownClientData(clientData);
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
