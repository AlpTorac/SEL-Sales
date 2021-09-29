package external;

import controller.IController;
import external.connection.IService;
import model.IModel;

public abstract class External implements IExternal {
	
	private IService service;
	private IModel model;
	private IController controller;
	
	protected External(IController controller, IModel model) {
		this.controller = controller;
		this.model = model;
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
}
