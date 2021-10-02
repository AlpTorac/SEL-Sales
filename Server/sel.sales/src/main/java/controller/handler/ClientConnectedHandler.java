package controller.handler;

import controller.IController;

public class ClientConnectedHandler extends StatusEventHandler {

	public ClientConnectedHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().clientConnected((String) args[0]);
	}

}
