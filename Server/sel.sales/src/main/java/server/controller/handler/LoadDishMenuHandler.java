package server.controller.handler;

import controller.handler.ServerApplicationEventHandler;
import server.controller.IServerController;

public class LoadDishMenuHandler extends ServerApplicationEventHandler {

	public LoadDishMenuHandler(IServerController controller) {
		super(controller);
		// TODO Auto-generated constructor stub
	}

	@Override
	public void handleApplicationEvent(Object[] args) {
		this.getController().getModel().loadDishMenu((String) args[0]);
	}
}
