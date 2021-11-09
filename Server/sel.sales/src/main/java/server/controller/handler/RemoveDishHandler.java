package server.controller.handler;

import controller.handler.ServerApplicationEventHandler;
import server.controller.IServerController;

public class RemoveDishHandler extends ServerApplicationEventHandler {

	public RemoveDishHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().removeMenuItem((String) args[0]);
	}
}
