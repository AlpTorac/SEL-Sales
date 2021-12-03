package test.external.dummy;

import java.util.concurrent.ExecutorService;

import client.controller.IClientController;
import client.external.ClientExternal;
import client.model.IClientModel;
import external.connection.IConnection;
import external.connection.IService;
import external.connection.outgoing.IExternalConnector;
import external.device.DeviceDiscoveryStrategy;
import external.device.IDeviceManager;

public class DummyClientExternal extends ClientExternal implements IDummyExternal {
	private String serviceID;
	private String serviceName;
	
	protected DummyClientExternal(String serviceID, String serviceName, IClientController controller, IClientModel model, long pingPongTimeout,
			long minimalPingPongDelay, long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.serviceID = serviceID;
		this.serviceName = serviceName;
		System.out.println("Service: " + this.getService());
	}

	public DummyClientExternal(String id, String name, IClientController controller, IClientModel model) {
		this(id, name, controller, model, DEFAULT_PP_TIMEOUT, DEFAULT_PP_MINIMAL_TIMEOUT, SEND_TIMEOUT, RESEND_LIMIT);
	}
	
	public ExecutorService getES() {
		return this.es;
	}
	
	public void setDiscoveryStrategy(DeviceDiscoveryStrategy cds) {
		System.out.println("Discovery strategy set");
//		this.cm.setDiscoveryStrategy(cds);
		this.getService().getDeviceManager().setDiscoveryStrategy(cds);
	}
	
	@Override
	protected IExternalConnector initConnector() {
		System.out.println("Connector service: " + this.getService());
		return new DummyExternalConnector(
				this.getService(),
				this.getController(),
				this.getES(),
				this.getPingPongTimeout(),
				this.getMinimalPingPongDelay(), this.getSendTimeout(), this.getResendLimit());
	}

	@Override
	protected IService initService() {
		return new DummyService(
				serviceID, 
				serviceName, 
				this.initDeviceManager(), 
				this.getController(), 
				this.getES(), 
				this.getPingPongTimeout(), 
				this.getMinimalPingPongDelay(), 
				this.getSendTimeout(), 
				this.getResendLimit());
	}

	@Override
	protected IDeviceManager initDeviceManager() {
		return new DummyDeviceManager(this.getES(), this.getController());
	}

	public IConnection getConnection(String deviceAddress) {
		return this.getConnector().getConnection(deviceAddress);
	}
	
}
