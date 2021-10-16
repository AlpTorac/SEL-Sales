package controller.handler;

import controller.IController;

public class ClientDisconnectedHandler extends StatusEventHandler {

	public ClientDisconnectedHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().clientDisconnected((String) args[0]);
	}

}
