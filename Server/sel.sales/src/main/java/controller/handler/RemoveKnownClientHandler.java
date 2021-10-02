package controller.handler;

import controller.IController;

public class RemoveKnownClientHandler extends StatusEventHandler {

	public RemoveKnownClientHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		String clientAddress = (String) args[0];
		if (clientAddress != null) {
			this.getController().removeKnownClient(clientAddress);
		}
	}

}
