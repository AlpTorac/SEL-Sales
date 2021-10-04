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
	
	protected External(IController controller, IModel model) {
		this.controller = controller;
		this.model = model;
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
	public void rediscoverClients() {
		this.service.getClientManager().discoverClients();
	}
	@Override
	public void refreshKnownClients() {
		this.service.getClientManager().receiveKnownClientData(this.model.getAllKnownClientData());
	}
}
