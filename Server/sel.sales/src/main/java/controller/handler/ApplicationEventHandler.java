package controller.handler;

import controller.IController;

public abstract class ApplicationEventHandler implements IApplicationEventHandler {

	protected IController controller;

	public ApplicationEventHandler(IController controller) {
		this.controller = controller;
	}

	protected IController getController() {
		return this.controller;
	}

}