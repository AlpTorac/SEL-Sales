package server.controller.handler;

import model.dish.DishMenuItemData;
import server.controller.IServerController;

public class AddDishHandler extends ServerApplicationEventHandler {

	public AddDishHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().addMenuItem((DishMenuItemData) args[0]);
	}
}
