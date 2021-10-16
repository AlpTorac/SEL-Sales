package controller.handler;

import controller.IController;

public class WriteDishMenuHandler extends BusinessEventHandler {

	public WriteDishMenuHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().writeDishMenu();
	}

}
