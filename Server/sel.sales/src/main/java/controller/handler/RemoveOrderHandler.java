package controller.handler;

import controller.IController;

public class RemoveOrderHandler extends BusinessEventHandler {

	public RemoveOrderHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().removeOrder((String) args[0]);
	}

}
