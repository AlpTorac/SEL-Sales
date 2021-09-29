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
	
	private IClientManager clientManager;
	private IServiceConnectionManager serviceConnectionManager;
	
	protected ServerExternal(IController controller, IModel model) {
		super(controller, model);
		this.setClientManager(this.initClientManager());
		this.setService(this.initService());
	}
	
	protected abstract IClientManager initClientManager();
	
	protected IServiceConnectionManager getServiceConnectionManager() {
		return serviceConnectionManager;
	}
	
	protected void setClientManager(IClientManager clientManager) {
		this.clientManager = clientManager;
	}
	
	protected IClientManager getClientManager() {
		return this.clientManager;
	}
	
	@Override
	public void refreshMenu() {
		this.sendMenuData();
	}
	
	public void sendMenuData() {
		IDishMenuData data = this.getModel().getMenuData();
		IBroadcaster broadcaster = new DishMenuBroadcaster(this.getServiceConnectionManager(),
				this.getModel().getExternalDishMenuSerialiser().serialise(data));
		broadcaster.broadcast();
	}
	
	@Override
	protected void setService(IService service) {
		super.setService(service);
		this.serviceConnectionManager = super.getService().publish();
	}
	
	@Override
	public void discoverClients() {
		this.getClientManager().discoverClients();
	}

	@Override
	public void addClient(String clientAddress) {
		this.getClientManager().addClient(clientAddress);
	}

	@Override
	public void removeClient(String clientAddress) {
		this.getClientManager().removeClient(clientAddress);
	}

	@Override
	public void allowClient(String clientAddress) {
		this.getClientManager().allowClient(clientAddress);
	}

	@Override
	public void blockClient(String clientAddress) {
		this.getClientManager().blockClient(clientAddress);
	}

	@Override
	public boolean isAllowedToConnect(String clientAddress) {
		return this.getClientManager().isAllowedToConnect(clientAddress);
	}
}
