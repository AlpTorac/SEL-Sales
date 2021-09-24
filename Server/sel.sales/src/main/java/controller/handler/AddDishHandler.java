package controller.handler;

import controller.IController;

public class AddDishHandler extends BusinessEventHandler {

	public AddDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().addMenuItem((String) args[0]);
	}
}
