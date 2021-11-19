package client.controller.handler;

import client.controller.IClientController;

public class MenuReceivedHandler extends ClientApplicationEventHandler {

	public MenuReceivedHandler(IClientController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().setDishMenuFromExternal((String) args[0]);
	}

}
