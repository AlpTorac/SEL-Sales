package client.external;

import client.controller.IClientController;
import client.model.IClientModel;
import external.External;
import external.connection.outgoing.IExternalConnector;
import external.message.Message;
import external.message.MessageContext;
import model.order.IOrderData;

public abstract class ClientExternal extends External implements IClientExternal {
	
	private IExternalConnector connector;
	
	protected ClientExternal(IClientController controller, IClientModel model, long pingPongTimeout, long minimalPingPongDelay,
			long sendTimeout, int resendLimit) {
		super(controller, model, pingPongTimeout, minimalPingPongDelay, sendTimeout, resendLimit);
		this.setService(this.initService());
		this.connector = this.initConnector();
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
		if (this.connector != null) {
			this.connector.receiveKnownDeviceData(this.getModel().getAllKnownDeviceData());
		}
	}
	
	@Override
	public void refreshOrders() {
		if (this.connector != null) {
			IOrderData[] orders = this.getModel().getAllPendingSendOrders();
			if (orders != null) {
				for (IOrderData order : orders) {
					this.connector.broadcastMessage(new Message(MessageContext.ORDER, null, this.getModel().getOrderHelper().serialiseForApp(order)));
				}
			}
		}
	}

}
