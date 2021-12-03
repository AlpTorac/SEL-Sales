package client.external;

import client.controller.IClientController;
import client.external.broadcaster.OrderBroadcaster;
import client.model.IClientModel;
import external.External;
import external.connection.outgoing.IExternalConnector;
import model.order.OrderData;

public abstract class ClientExternal extends External implements IClientExternal {
	
	private IExternalConnector connector;
	
	protected ClientExternal(IClientController controller, IClientModel model, long pingPongTimeout, long minimalPingPongDelay,
			long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
	}
	
	protected void setupConnector() {
		this.connector = this.initConnector();
	}
	
	protected IExternalConnector getConnector() {
		if (this.connector == null) {
			this.setupConnector();
		}
		return this.connector;
	}
	
	@Override
	protected IClientModel getModel() {
		return (IClientModel) super.getModel();
	}
	
	@Override
	protected IClientController getController() {
		return (IClientController) super.getController();
	}
	
	protected abstract IExternalConnector initConnector();
	
	@Override
	public void refreshKnownDevices() {
		super.refreshKnownDevices();
		if (this.getConnector() != null) {
			this.getConnector().receiveKnownDeviceData(this.getModel().getAllKnownDeviceData());
		}
	}
	
	@Override
	public void refreshOrders() {
		if (this.getConnector() != null) {
			OrderData[] orders = this.getModel().getAllPendingSendOrders();
			if (orders != null) {
				for (OrderData order : orders) {
					new OrderBroadcaster(this.getConnector(), this.getModel(), order).broadcast();
//					this.connector.broadcastMessage(new Message(MessageContext.ORDER, null, this.getModel().getOrderHelper().serialiseForApp(order)));
				}
			}
		}
	}
	
	@Override
	public void close() {
		super.close();
		if (this.getConnector() != null) {
			this.getConnector().close();
		}
	}
}
