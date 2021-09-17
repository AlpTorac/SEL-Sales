package external;

import controller.IController;
import model.IModel;

public abstract class External implements IExternal {

	private IModel model;
	private IController controller;
	
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
}
