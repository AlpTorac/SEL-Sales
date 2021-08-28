package controller;

import model.dish.IDishMenuItemID;

public class RemoveDishHandler extends BusinessEventHandler {

	RemoveDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().removeMenuItem((IDishMenuItemID) args[0]);
	}
}
