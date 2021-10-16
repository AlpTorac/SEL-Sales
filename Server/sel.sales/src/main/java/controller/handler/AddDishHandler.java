package controller.handler;

import controller.IController;

public class AddDishHandler extends BusinessEventHandler {

	public AddDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().addMenuItem((String) args[0]);
	}
}
