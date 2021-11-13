package server.controller.handler;

import server.controller.IServerController;

public class WriteDishMenuHandler extends ServerApplicationEventHandler {

	public WriteDishMenuHandler(IServerController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().writeDishMenu();
	}

}
