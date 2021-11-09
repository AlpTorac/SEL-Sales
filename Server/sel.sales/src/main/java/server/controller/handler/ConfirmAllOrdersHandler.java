package server.controller.handler;

import controller.handler.ServerApplicationEventHandler;
import server.controller.IServerController;

public class ConfirmAllOrdersHandler extends ServerApplicationEventHandler {
	public ConfirmAllOrdersHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().confirmAllOrders();
	}
}
