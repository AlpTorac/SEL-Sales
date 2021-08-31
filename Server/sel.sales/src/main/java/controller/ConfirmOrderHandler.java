package controller;

import model.order.IOrderData;

public class ConfirmOrderHandler extends BusinessEventHandler {

	ConfirmOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().confirmOrder((IOrderData) args[0]);
	}

}
