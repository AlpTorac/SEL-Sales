package server.view;

import model.OrderConfirmationStatusUpdatable;
import server.controller.IServerController;
import server.model.IServerModel;
import view.IView;

public interface IServerView extends IView, OrderConfirmationStatusUpdatable {
	@Override
	IServerController getController();
	@Override
	IServerModel getModel();
}
