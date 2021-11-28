package client.controller.handler;

import client.controller.IClientController;
import client.controller.handler.ClientApplicationEventHandler;

public class RemoveOrderHandler extends ClientApplicationEventHandler {

	public RemoveOrderHandler(IClientController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().removeOrder((String) args[0]);
	}
}
