package server.external;

import external.External;
import server.controller.IServerController;
import server.external.broadcaster.DishMenuBroadcaster;
import server.model.IServerModel;

public abstract class ServerExternal extends External implements IServerExternal {
	
	protected ServerExternal(IServerController controller, IServerModel model,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.setService(this.initService());
	}
	
//	protected abstract IDeviceManager initDeviceManager();
	
//	protected IServiceConnectionManager getServiceConnectionManager() {
//		return this.getService().getServiceConnectionManager();
//	}
//	
//	protected IDeviceManager getDeviceManager() {
//		return this.getService().getDeviceManager();
//	}
	
	@Override
	public void refreshMenu() {
		this.sendMenuData();
	}
	
	protected void sendMenuData() {
		new DishMenuBroadcaster(this.getServiceConnectionManager(), this.getModel()).broadcast();
	}
	
//	@Override
//	protected void setService(IService service) {
//		super.setService(service);
//		super.getService().publish();
//	}
	
	@Override
	protected IServerModel getModel() {
		return (IServerModel) super.getModel();
	}
	
	@Override
	protected IServerController getController() {
		return (IServerController) super.getController();
	}
}
