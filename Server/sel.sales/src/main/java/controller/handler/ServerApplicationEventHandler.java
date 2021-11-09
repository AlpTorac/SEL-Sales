package controller.handler;

import server.controller.IServerController;

public abstract class ServerApplicationEventHandler extends ApplicationEventHandler {
	protected ServerApplicationEventHandler(IServerController controller) {
		super(controller);
	}
	
	@Override
	protected IServerController getController() {
		return (IServerController) super.getController();
	}
}
