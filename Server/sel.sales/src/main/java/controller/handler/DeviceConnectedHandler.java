package controller.handler;

import controller.IController;

public class DeviceConnectedHandler extends ApplicationEventHandler {

	public DeviceConnectedHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().deviceConnected((String) args[0]);
	}

}
