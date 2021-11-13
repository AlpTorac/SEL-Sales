package server.controller.handler;

import server.controller.IServerController;

public class WriteOrdersHandler extends ServerApplicationEventHandler {
	public WriteOrdersHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().writeOrders();
	}
}
