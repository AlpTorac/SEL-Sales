package client.controller.handler;

import client.controller.IClientController;

public class AddPendingPaymentOrderHandler extends ClientApplicationEventHandler {

	public AddPendingPaymentOrderHandler(IClientController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().makePendingPaymentOrder((String) args[0]);
	}

}
