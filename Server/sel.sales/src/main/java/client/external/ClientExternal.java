package client.external;

import client.controller.IClientController;
import client.model.IClientModel;
import external.External;

public abstract class ClientExternal extends External implements IClientExternal {

	protected ClientExternal(IClientController controller, IClientModel model, long pingPongTimeout, long minimalPingPongDelay,
			long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.setService(this.initService());
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
	public void refreshOrders() {
		// TODO Auto-generated method stub
		
	}

}
