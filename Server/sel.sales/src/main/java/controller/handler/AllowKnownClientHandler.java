package controller.handler;

import controller.IController;

public class AllowKnownClientHandler extends StatusEventHandler {

	public AllowKnownClientHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().allowKnownClient((String) args[0]);
	}

}
