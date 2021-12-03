package test.external.dummy;

import client.controller.IClientController;
import client.controller.StandardClientController;
import client.external.ClientExternal;
import client.model.ClientModel;
import client.model.IClientModel;
import controller.IController;
import model.IModel;
import model.order.OrderData;

public class DummyClient extends DummyInteractionPartaker {

	public DummyClient(String serviceID, String serviceName, String name, String address) {
		super(serviceID, serviceName, name, address);
	}
	
	@Override
	protected IModel initModel() {
		return new ClientModel(this.getTestFolderAddress());
	}

	@Override
	protected IController initController() {
		return new StandardClientController(this.getModel());
	}

	@Override
	protected IDummyExternal initExternal() {
		return new DummyClientExternal(this.getServiceID(),
				this.getServiceName(), this.getController(), this.getModel());
	}
	
	@Override
	public IClientModel getModel() {
		return (IClientModel) super.getModel();
	}
	
	@Override
	public IClientController getController() {
		return (IClientController) super.getController();
	}
	
	@Override
	public IDummyExternal getExternal() {
		return (IDummyExternal) super.getExternal();
	}
	
	public void refreshOrders() {
		((ClientExternal) this.getExternal()).refreshOrders();
	}
	
	public OrderData[] getAllSentOrders() {
		return this.getModel().getAllSentOrders();
	}
	
	public OrderData[] getAllCookingOrders() {
		return this.getModel().getAllCookingOrders();
	}
	
	public OrderData[] getAllPendingPaymentOrders() {
		return this.getModel().getAllPendingPaymentOrders();
	}
	
	public OrderData[] getAllPendingSendOrders() {
		return this.getModel().getAllPendingSendOrders();
	}
	
	public OrderData[] getAllWrittenOrders() {
		return this.getModel().getAllWrittenOrders();
	}
	
	public void addCookingOrder(String serialisedOrder) {
		this.getModel().addCookingOrder(serialisedOrder);
	}
	
	public void makePendingPaymentOrder(String orderID) {
		this.getModel().makePendingPaymentOrder(orderID);
	}
	
	public void makePendingSendOrder(String formerID, String serialisedOrder) {
		this.getModel().makePendingSendOrder(serialisedOrder);
	}
	
	public void makeSentOrder(String orderID) {
		this.getModel().makeSentOrder(orderID);
	}
}
