package server.view;

import server.controller.IServerController;
import server.model.IServerModel;
import view.IView;

public interface IServerView extends IView {
	@Override
	IServerController getController();
	@Override
	IServerModel getModel();
}
