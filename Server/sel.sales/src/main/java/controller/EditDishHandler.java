package controller;

import model.dish.IDishMenuItemData;

public class EditDishHandler extends BusinessEventHandler {
	
	EditDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().editMenuItem((IDishMenuItemData) args[0]);
	}
}
