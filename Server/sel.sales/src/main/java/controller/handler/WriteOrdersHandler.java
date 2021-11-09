package controller.handler;

import controller.IController;

public class WriteOrdersHandler extends ApplicationEventHandler {
	public WriteOrdersHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().writeOrders();
	}
}
