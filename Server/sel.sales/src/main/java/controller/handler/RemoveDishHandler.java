package controller.handler;

import controller.IController;

public class RemoveDishHandler extends BusinessEventHandler {

	public RemoveDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().removeMenuItem((String) args[0]);
	}
}
