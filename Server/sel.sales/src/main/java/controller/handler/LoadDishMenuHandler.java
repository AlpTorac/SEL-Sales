package controller.handler;

import controller.IController;

public class LoadDishMenuHandler extends BusinessEventHandler {

	public LoadDishMenuHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().loadDishMenu((String) args[0]);
	}
}
