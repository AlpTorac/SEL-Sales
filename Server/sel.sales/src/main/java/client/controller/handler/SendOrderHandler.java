package client.controller.handler;

import client.controller.IClientController;

public class SendOrderHandler extends ClientApplicationEventHandler {

	public SendOrderHandler(IClientController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().makePendingSendOrder((String) args[0], (String) args[1]);
	}

}
