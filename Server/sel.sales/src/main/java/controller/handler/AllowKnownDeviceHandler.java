package controller.handler;

import controller.IController;

public class AllowKnownDeviceHandler extends ApplicationEventHandler {

	public AllowKnownDeviceHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().allowKnownDevice((String) args[0]);
	}

}
