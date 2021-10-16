package controller.handler;

import controller.IController;

public class AddKnownClientHandler extends StatusEventHandler {

	public AddKnownClientHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		String clientAddress = (String) args[0];
		if (clientAddress != null) {
			this.getController().getModel().addKnownClient(clientAddress);
		}
	}
}
