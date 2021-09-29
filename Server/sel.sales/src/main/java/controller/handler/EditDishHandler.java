package controller.handler;

import controller.IController;

public class EditDishHandler extends BusinessEventHandler {
	
	public EditDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().editMenuItem((String) args[0]);
	}
}
