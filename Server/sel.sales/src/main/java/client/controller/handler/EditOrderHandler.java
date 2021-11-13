package client.controller.handler;

import client.controller.IClientController;

public class EditOrderHandler extends ClientApplicationEventHandler {

	public EditOrderHandler(IClientController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().editOrder((String) args[0]);
	}
}
