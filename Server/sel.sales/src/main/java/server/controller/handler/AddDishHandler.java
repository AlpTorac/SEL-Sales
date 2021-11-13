package server.controller.handler;

import server.controller.IServerController;

public class AddDishHandler extends ServerApplicationEventHandler {

	public AddDishHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().addMenuItem((String) args[0]);
	}
}
