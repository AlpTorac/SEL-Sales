package controller.handler;

import controller.IController;
import model.connectivity.IClientData;

public class KnownClientAddedHandler extends StatusEventHandler {

	KnownClientAddedHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().addKnownClient((IClientData) args[0]);
	}
}
