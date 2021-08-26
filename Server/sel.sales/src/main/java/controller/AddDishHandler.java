package controller;

import model.IDishMenuItemData;

public class AddDishHandler extends BusinessEventHandler {

	AddDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().addMenuItem((IDishMenuItemData) args[0]);
	}
}
