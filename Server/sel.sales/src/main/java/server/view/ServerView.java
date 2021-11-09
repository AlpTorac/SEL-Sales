package server.view;

import server.controller.IServerController;
import server.model.IServerModel;
import view.View;

public abstract class ServerView extends View implements IServerView {
	public ServerView(IServerController controller, IServerModel model) {
		super(controller, model);
	}
	
	@Override
	public IServerController getController() {
		return (IServerController) super.getController();
	}
	
	@Override
	public IServerModel getModel() {
		return (IServerModel) super.getModel();
	}
}
