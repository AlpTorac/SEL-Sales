package server.controller.handler;

import server.controller.IServerController;

public class EditDishHandler extends ServerApplicationEventHandler {
	
	public EditDishHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().editMenuItem((String) args[0]);
	}
}
