package controller.handler;

import controller.IController;

public class RemoveKnownDeviceHandler extends ApplicationEventHandler {

	public RemoveKnownDeviceHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		String deviceAddress = (String) args[0];
		if (deviceAddress != null) {
			this.getController().getModel().removeKnownDevice(deviceAddress);
		}
	}

}
