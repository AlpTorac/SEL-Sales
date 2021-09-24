package controller.handler;

import controller.IController;

public class RemoveDishHandler extends BusinessEventHandler {

	public RemoveDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().removeMenuItem((String) args[0]);
	}
}
