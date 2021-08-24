package view;

import controller.IController;

public abstract class View implements IView {
	private IController controller;
	
	public View(IController controller) {
		this.controller = controller;
	}
	
	public IController getController() {
		return this.controller;
	}
	
	public void setController(IController controller) {
		this.controller = controller;
	}
}
