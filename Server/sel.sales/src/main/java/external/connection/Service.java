package external.connection;

import controller.IController;
import external.client.IClientManager;

public abstract class Service implements IService {
	private IServiceConnectionManager scm;
	private IClientManager clientManager;
	
	private String id;
	private String url;
	private String name;
	
	private IController controller;
	
	public Service(String id, String name, IClientManager clientManager, IController controller) {
		this.id = id;
		this.name = name;
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

}
