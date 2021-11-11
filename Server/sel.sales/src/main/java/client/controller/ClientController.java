package client.controller;

import client.model.IClientModel;
import controller.Controller;
import controller.manager.IApplicationEventManager;

public abstract class ClientController extends Controller implements IClientController {

	public ClientController(IClientModel model) {
		super(model);
	}

	@Override
	public IClientModel getModel() {
		return (IClientModel) super.getModel();
	}

	@Override
	protected IApplicationEventManager initEventManager() {
		IApplicationEventManager bem = super.initEventManager();
		return bem;
	}
}
