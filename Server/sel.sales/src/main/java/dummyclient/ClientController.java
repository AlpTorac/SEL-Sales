package dummyclient;

import controller.Controller;
import controller.manager.IApplicationEventManager;
import model.IModel;

public class ClientController extends Controller {

	public ClientController(IModel model) {
		super(model);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected IApplicationEventManager initEventManager() {
		// TODO Auto-generated method stub
		return null;
	}

}
