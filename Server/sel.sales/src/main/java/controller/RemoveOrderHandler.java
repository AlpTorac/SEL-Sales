package controller;

import model.order.IOrderID;

public class RemoveOrderHandler extends BusinessEventHandler {

	RemoveOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().removeOrder((IOrderID) args[0]);
	}

}
