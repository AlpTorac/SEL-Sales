package controller.handler;

import controller.IController;

public class AddDiscoveredClientHandler extends StatusEventHandler {

	public AddDiscoveredClientHandler(IController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().addDiscoveredClient((String) args[0], (String) args[1]);
	}

}
