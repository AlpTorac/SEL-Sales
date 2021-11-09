package controller.handler;

import controller.IController;

public class AddDiscoveredDeviceHandler extends ApplicationEventHandler {

	public AddDiscoveredDeviceHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().addDiscoveredDevice((String) args[0], (String) args[1]);
	}

}
