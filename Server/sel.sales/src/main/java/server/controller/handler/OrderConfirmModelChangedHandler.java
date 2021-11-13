package server.controller.handler;

import server.controller.IServerController;

public class OrderConfirmModelChangedHandler extends ServerApplicationEventHandler {

	public OrderConfirmModelChangedHandler(IServerController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().setAutoConfirmOrders((boolean) args[0]);
	}
}
