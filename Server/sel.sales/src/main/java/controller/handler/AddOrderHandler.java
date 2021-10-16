package controller.handler;

import controller.IController;

public class AddOrderHandler extends BusinessEventHandler {

	public AddOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().addUnconfirmedOrder((String) args[0]);
	}

}
