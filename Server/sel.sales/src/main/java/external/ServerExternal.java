package external;

import controller.IController;
import external.broadcaster.DishMenuBroadcaster;
import external.broadcaster.IBroadcaster;
import external.client.IClientManager;
import external.connection.IService;
import external.connection.IServiceConnectionManager;
import model.IModel;
import model.MenuUpdatable;
import model.dish.IDishMenuData;

public abstract class ServerExternal extends External implements IServerExternal, MenuUpdatable {
	
	protected ServerExternal(IController controller, IModel model,
			long pingPongTimeout, long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.setService(this.initService());
	}
	
	protected abstract IClientManager initClientManager();
	
	protected IServiceConnectionManager getServiceConnectionManager() {
		return this.getService().getServiceConnectionManager();
	}
	
	protected IClientManager getClientManager() {
		return this.getService().getClientManager();
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
