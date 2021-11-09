package client.external;

import client.controller.IClientController;
import client.model.IClientModel;
import external.External;
import external.connection.IService;

public abstract class ClientExternal extends External implements IClientExternal {

	protected ClientExternal(IClientController controller, IClientModel model, long pingPongTimeout, long minimalPingPongDelay,
			long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	
	@Override
	protected IClientModel getModel() {
		return (IClientModel) super.getModel();
	}
	
	@Override
	protected IClientController getController() {
		return (IClientController) super.getController();
	}
	
	@Override
	protected IService initService() {
		// TODO Auto-generated method stub
		return null;
	}

}
