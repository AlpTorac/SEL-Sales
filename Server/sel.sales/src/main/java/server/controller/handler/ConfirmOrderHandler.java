package server.controller.handler;

import server.controller.IServerController;

public class ConfirmOrderHandler extends ServerApplicationEventHandler {

	public ConfirmOrderHandler(IServerController controller) {
		super(controller);
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().confirmOrder((String) args[0]);
	}

}
