package controller.handler;

import controller.IController;

public class OrderConfirmModelChangedHandler extends StatusEventHandler {

	public OrderConfirmModelChangedHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().setAutoConfirmOrders((boolean) args[0]);
	}
}
