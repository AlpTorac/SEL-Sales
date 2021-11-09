package client.view;

import client.controller.IClientController;
import client.model.IClientModel;
import view.IView;

public interface IClientView extends IView {
	@Override
	IClientController getController();
	@Override
	IClientModel getModel();
}
