package test.external.dummy;

import controller.IController;
import external.External;
import external.ServerExternal;
import external.client.ClientDiscoveryStrategy;
import external.client.IClientManager;
import external.connection.IService;
import model.IModel;

public class DummyExternal extends External {
//	private IClientManager cm;
	private String id;
	private String name;
	
	public DummyExternal(String id, String name, IController controller, IModel model, long pingPongTimeout, long minimalPingPongDelay,
			long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
//		this.cm = new DummyClientManager(es, controller);
		this.id = id;
		this.name = name;
		this.setService(this.initService());
	}

	public void setDiscoveryStrategy(ClientDiscoveryStrategy cds) {
		System.out.println("Discovery strategy set");
//		this.cm.setDiscoveryStrategy(cds);
		this.getService().getClientManager().setDiscoveryStrategy(cds);
	}
	
//	@Override
//	public void rediscoverClients() {
//		System.out.println("Rediscovering");
//		this.cm.discoverClients();
//	}
//	@Override
//	public void refreshKnownClients() {
//		System.out.println("Refreshing known clients");
//		if (this.cm != null) {
//			this.cm.receiveKnownClientData(this.getModel().getAllKnownClientData());
//		}
//	}
	@Override
	protected IService initService() {
		return new DummyService(
				id, 
				name, 
				new DummyClientManager(es, this.getController()), 
				this.getController(), 
				es, 
				this.getPingPongTimeout(), 
				this.getMinimalPingPongDelay(), 
				this.getSendTimeout(), 
				this.getResendLimit());
	}

}
