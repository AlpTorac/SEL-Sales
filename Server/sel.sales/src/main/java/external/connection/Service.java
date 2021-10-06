package external.connection;

import java.util.concurrent.ExecutorService;

import controller.IController;
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
	
	public Service(String id, String name, IClientManager clientManager, IController controller, ExecutorService es) {
		this.id = id;
		this.name = name;
		this.es = es;
		this.clientManager = clientManager;
		this.controller = controller;
		this.url = this.generateURL();
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
}
