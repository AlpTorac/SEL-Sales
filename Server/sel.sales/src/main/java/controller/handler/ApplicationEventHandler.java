package controller.handler;

import controller.IController;

public abstract class ApplicationEventHandler implements IApplicationEventHandler {
	private IController controller;
	
	ApplicationEventHandler(IController controller) {
		this.controller = controller;
	}
	
	IController getController() {
		return this.controller;
	}
}
