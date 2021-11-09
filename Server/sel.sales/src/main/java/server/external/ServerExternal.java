package server.external;

import controller.IController;
import external.External;
import external.broadcaster.DishMenuBroadcaster;
import external.broadcaster.IBroadcaster;
import external.device.IDeviceManager;
import external.connection.IService;
import external.connection.IServiceConnectionManager;
import model.IModel;
import model.MenuUpdatable;

public abstract class ServerExternal extends External implements IServerExternal, MenuUpdatable {
	
	protected ServerExternal(IController controller, IModel model,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.setService(this.initService());
	}
	
	protected abstract IDeviceManager initDeviceManager();
	
	protected IServiceConnectionManager getServiceConnectionManager() {
		return this.getService().getServiceConnectionManager();
	}
	
	protected IDeviceManager getDeviceManager() {
		return this.getService().getDeviceManager();
	}
	
	@Override
	public void refreshMenu() {
		this.sendMenuData();
	}
	
	public void sendMenuData() {
		IBroadcaster broadcaster = new DishMenuBroadcaster(this.getServiceConnectionManager(), this.getModel());
		broadcaster.broadcast();
	}
	
	@Override
	protected void setService(IService service) {
		super.setService(service);
		super.getService().publish();
	}
}
