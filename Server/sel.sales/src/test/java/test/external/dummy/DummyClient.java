package test.external.dummy;

import client.controller.IClientController;
import client.controller.StandardClientController;
import client.model.ClientModel;
import client.model.IClientModel;
import client.view.IClientView;
import client.view.StandardClientView;
import controller.IController;
import model.IModel;
import model.order.IOrderData;
import view.IView;
import view.repository.uifx.FXUIComponentFactory;
import view.repository.uiwrapper.UIComponentFactory;

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
	public DummyClientExternal getExternal() {
		return (DummyClientExternal) super.getExternal();
	}
	
	public void refreshOrders() {
		this.getExternal().refreshOrders();
	}
	
	public IOrderData[] getAllSentOrders() {
		return this.getModel().getAllSentOrders();
	}
	
	public IOrderData[] getAllCookingOrders() {
		return this.getModel().getAllCookingOrders();
	}
	
	public IOrderData[] getAllPendingPaymentOrders() {
		return this.getModel().getAllPendingPaymentOrders();
	}
	
	public IOrderData[] getAllPendingSendOrders() {
		return this.getModel().getAllPendingSendOrders();
	}
	
	public IOrderData[] getAllWrittenOrders() {
		return this.getModel().getAllWrittenOrders();
	}
	
	public void addCookingOrder(String serialisedOrder) {
		this.getModel().addCookingOrder(serialisedOrder);
	}
	
	public void makePendingPaymentOrder(String orderID) {
		this.getModel().makePendingPaymentOrder(orderID);
	}
	
	public void makePendingSendOrder(String formerID, String serialisedOrder) {
		this.getModel().makePendingSendOrder(formerID, serialisedOrder);
	}
	
	public void makeSentOrder(String orderID) {
		this.getModel().makeSentOrder(orderID);
	}
}
