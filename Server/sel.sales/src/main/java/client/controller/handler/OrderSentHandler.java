package client.controller.handler;

import client.controller.IClientController;

public class OrderSentHandler extends ClientApplicationEventHandler {

	public OrderSentHandler(IClientController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().orderSent((String) args[0]);
	}
}
