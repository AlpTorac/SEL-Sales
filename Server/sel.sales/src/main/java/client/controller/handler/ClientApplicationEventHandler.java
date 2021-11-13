package client.controller.handler;

import client.controller.IClientController;
import controller.handler.ApplicationEventHandler;

public abstract class ClientApplicationEventHandler extends ApplicationEventHandler {

	public ClientApplicationEventHandler(IClientController controller) {
		super(controller);
	}

	@Override
	protected IClientController getController() {
		return (IClientController) super.getController();
	}

}
