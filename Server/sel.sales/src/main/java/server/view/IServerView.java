package server.view;

import server.controller.IServerController;
import server.model.IServerModel;
import server.model.OrderConfirmationStatusUpdatable;
import view.IView;

public interface IServerView extends IView, OrderConfirmationStatusUpdatable {
	@Override
	IServerController getController();
	@Override
	IServerModel getModel();
}
