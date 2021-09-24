package external;

import controller.IController;
import external.client.IClientManager;
import external.connection.IService;
import external.connection.IServiceConnectionManager;
import model.IModel;

public abstract class External implements IExternal {

	private IClientManager clientManager;
	private IService service;
	private IServiceConnectionManager serviceConnectionManager;
	private IModel model;
	private IController controller;
	
	External(IController controller, IModel model) {
		this.controller = controller;
		this.model = model;
		this.setClientManager(this.initClientManager());
		this.setService(this.initService());
	}
	
	protected IClientManager getClientManager() {
		return this.clientManager;
	}
	
	protected void setClientManager(IClientManager clientManager) {
		this.clientManager = clientManager;
	}
	
	protected abstract IClientManager initClientManager();
	
	protected abstract IService initService();
	
	@Override
	public void subscribe() {
		this.model.subscribe(this);
	}
	@Override
	public IController getController() {
		return this.controller;
	}
	@Override
	public IModel getModel() {
		return this.model;
	}

	protected IService getService() {
		return service;
	}

	protected IServiceConnectionManager getServiceConnectionManager() {
		return serviceConnectionManager;
	}
	
	protected void setService(IService service) {
		this.service = service;
		this.serviceConnectionManager = this.service.publish();
	}
	
}
