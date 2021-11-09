package controller.handler;

import controller.IController;

public class AddOrderHandler extends ApplicationEventHandler {

	public AddOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().addOrder((String) args[0]);
	}

}
