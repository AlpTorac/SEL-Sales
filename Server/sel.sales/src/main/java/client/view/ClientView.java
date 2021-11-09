package client.view;

import client.controller.IClientController;
import client.model.IClientModel;
import view.View;

public abstract class ClientView extends View implements IClientView {

	public ClientView(IClientController controller, IClientModel model) {
		super(controller, model);
	}

	@Override
	public IClientController getController() {
		return (IClientController) super.getController();
	}

	@Override
	public IClientModel getModel() {
		return (IClientModel) super.getModel();
	}

}
