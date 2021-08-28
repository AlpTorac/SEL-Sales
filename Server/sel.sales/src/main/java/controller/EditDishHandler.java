package controller;

import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemID;

public class EditDishHandler extends BusinessEventHandler {
	
	EditDishHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleBusinessEvent(Object[] args) {
		this.getController().removeMenuItem((IDishMenuItemID) args[0]);
		this.getController().addMenuItem((IDishMenuItemData) args[1]);
	}
}
