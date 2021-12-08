package server.controller.handler;

import server.controller.IServerController;

public class ExportOrdersHandler extends ServerApplicationEventHandler {
	public ExportOrdersHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().exportOrders();
	}
}
