package controller.handler;

import controller.IController;

public class AddKnownDeviceHandler extends ApplicationEventHandler {

	public AddKnownDeviceHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		String deviceAddress = (String) args[0];
		if (deviceAddress != null) {
			this.getController().getModel().addKnownDevice(deviceAddress);
		}
	}
}
