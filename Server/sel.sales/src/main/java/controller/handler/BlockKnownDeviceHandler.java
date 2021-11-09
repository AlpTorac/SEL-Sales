package controller.handler;

import controller.IController;

public class BlockKnownDeviceHandler extends ApplicationEventHandler {

	public BlockKnownDeviceHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().blockKnownDevice((String) args[0]);
	}

}
