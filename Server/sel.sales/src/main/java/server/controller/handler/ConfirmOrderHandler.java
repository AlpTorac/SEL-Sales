package server.controller.handler;

import model.order.OrderData;
import server.controller.IServerController;

public class ConfirmOrderHandler extends ServerApplicationEventHandler {

	public ConfirmOrderHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().confirmOrder((OrderData) args[0]);
	}

}
