package controller.handler;

import controller.IController;

public class ConfirmAllOrdersHandler extends BusinessEventHandler {
	public ConfirmAllOrdersHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().confirmAllOrders();
	}
}
