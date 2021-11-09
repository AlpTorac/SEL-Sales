package controller.handler;

import controller.IController;

public class DeviceDisconnectedHandler extends ApplicationEventHandler {

	public DeviceDisconnectedHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().deviceDisconnected((String) args[0]);
	}

}
