package client.controller.handler;

import client.controller.IClientController;
import model.order.OrderData;

public class SendOrderHandler extends ClientApplicationEventHandler {

	public SendOrderHandler(IClientController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().makePendingSendOrder((OrderData) args[0]);
	}

}
