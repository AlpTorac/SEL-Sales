package controller.handler;

import controller.IController;

public class DishMenuFolderChangedHandler extends StatusEventHandler {

	public DishMenuFolderChangedHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().setDishMenuFolderAddress((String) args[0]);
	}

}
