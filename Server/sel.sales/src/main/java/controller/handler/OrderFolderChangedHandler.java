package controller.handler;

import controller.IController;

public class OrderFolderChangedHandler extends StatusEventHandler {

	public OrderFolderChangedHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().setOrderFolderAddress((String) args[0]);
	}

}
