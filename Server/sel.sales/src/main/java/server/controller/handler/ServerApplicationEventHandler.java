package server.controller.handler;

import controller.handler.ApplicationEventHandler;
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
