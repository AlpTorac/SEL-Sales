package controller.handler;

import controller.IController;

public class DiscoverClientsHandler extends BusinessEventHandler {
	public DiscoverClientsHandler(IController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().requestClientRediscovery();
	}
}
