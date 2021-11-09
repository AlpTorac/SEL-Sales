package client.controller;

import client.model.IClientModel;
import controller.Controller;

public abstract class ClientController extends Controller implements IClientController {

	public ClientController(IClientModel model) {
		super(model);
	}

	@Override
	public IClientModel getModel() {
		return (IClientModel) super.getModel();
	}

}
