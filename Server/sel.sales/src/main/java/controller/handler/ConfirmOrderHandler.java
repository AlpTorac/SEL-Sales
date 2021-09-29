package controller.handler;

import controller.IController;

public class ConfirmOrderHandler extends BusinessEventHandler {

	public ConfirmOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().confirmOrder((String) args[0]);
	}

}
