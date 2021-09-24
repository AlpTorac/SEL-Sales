package controller.handler;

import controller.IController;

public class RemoveOrderHandler extends BusinessEventHandler {

	public RemoveOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().removeOrder((String) args[0]);
	}

}