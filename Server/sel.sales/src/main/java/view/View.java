package view;

import controller.IController;
import model.IModel;

public abstract class View implements IView {
	private IController controller;
	private IModel model;
	
	public View(IController controller, IModel model) {
		this.controller = controller;
		this.model = model;
	}
	
	public IController getController() {
		return this.controller;
	}
	
	public void setController(IController controller) {
		this.controller = controller;
	}
	
	public IModel getModel() {
		return this.model;
	}
	
	public void setModel(IModel model) {
		this.model = model;
	}
	
	public void startUp() {
		this.subscribe();
	}
	
	public abstract void show();
}
