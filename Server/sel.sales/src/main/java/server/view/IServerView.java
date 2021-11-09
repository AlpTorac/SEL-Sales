package server.view;

import model.OrderUpdatable;
import server.controller.IServerController;
import server.model.IServerModel;
import view.IView;

public interface IServerView extends IView, OrderUpdatable {
	@Override
	IServerController getController();
	@Override
	IServerModel getModel();
}
