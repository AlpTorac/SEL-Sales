package client.model;

import model.Model;
import model.order.IOrderCollector;
import model.order.IOrderData;

public class ClientModel extends Model implements IClientModel {
	
	/**
	 * Orders that have been taken and are cooking at the moment
	 */
	private IOrderCollector cookingOrders;
	/**
	 * Orders that have been cooked and served
	 */
	private IOrderCollector pendingPaymentOrders;
	/**
	 * Orders that are to be sent to the server
	 */
	private IOrderCollector pendingSendOrders;
	/**
	 * Orders that have already been sent to the server
	 */
	private IOrderCollector sentOrders;
	
	private String editOrderID;
	
	public ClientModel() {
		super();
		
		this.cookingOrders = this.getOrderHelper().createOrderCollector();
		this.pendingPaymentOrders = this.getOrderHelper().createOrderCollector();
		this.pendingSendOrders = this.getOrderHelper().createOrderCollector();
		this.sentOrders = this.getOrderHelper().createOrderCollector();
	}
	
	public ClientModel(String resourceFolder) {
		this();
		this.getFileManager().setResourcesFolderAddress(resourceFolder);
	}
	
	@Override
	public void addOrder(String serialisedOrderData) {
		this.addCookingOrder(serialisedOrderData);
	}

	@Override
	public IOrderData getOrder(String id) {
		if (id == null) {
			return null;
		}
		IOrderData data = null;
		if ((data = this.getCookingOrder(id)) != null) {
			return data;
		}
		if ((data = this.getPendingPaymentOrder(id)) != null) {
			return data;
		}
		if ((data = this.getPendingSendOrder(id)) != null) {
			return data;
		}
		if ((data = this.getSentOrder(id)) != null) {
			return data;
		}
		return data;
	}

	@Override
	public void removeAllOrders() {
		this.cookingOrders.clearOrders();
		this.pendingPaymentOrders.clearOrders();
		this.pendingSendOrders.clearOrders();
		this.sentOrders.clearOrders();
		this.ordersChanged();
	}

	@Override
	public void removeOrder(String id) {
		this.cookingOrders.removeOrder(id);
		this.pendingPaymentOrders.removeOrder(id);
		this.pendingSendOrders.removeOrder(id);
		this.sentOrders.removeOrder(id);
		this.ordersChanged();
	}

	@Override
	public void addCookingOrder(String serialisedOrderData) {
		IOrderData data = this.getOrderHelper().deserialiseOrderData(serialisedOrderData);
		if (this.getOrder(data.getID().toString()) == null) {
			if (this.editOrderID != null) {
				this.removeOrder(editOrderID);
				this.clearEditTarget();
			}
			this.cookingOrders.addOrder(data);
			this.ordersChanged();
		}
	}

	@Override
	public void makePendingPaymentOrder(String orderID) {
		IOrderData data;
		if ((data = this.getCookingOrder(orderID)) != null) {
			this.pendingPaymentOrders.addOrder(data);
			this.cookingOrders.removeOrder(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public void makePendingSendOrder(String formerID, String serialisedOrderData) {
		IOrderData data = this.getOrderHelper().deserialiseOrderData(serialisedOrderData);
		if (data != null && this.getPendingPaymentOrder(formerID) != null) {
			this.pendingSendOrders.addOrder(data);
			if (!formerID.equals(data.getID().toString())) {
				this.removeOrder(formerID);
			}
			this.pendingPaymentOrders.removeOrder(data.getID().toString());
			if (!this.isOrderWritten(data.getID().toString())) {
				this.writeOrder(data.getID().toString());
			}
			this.ordersChanged();
		}
	}

	@Override
	public void makeSentOrder(String orderID) {
		IOrderData data;
		if ((data = this.getPendingSendOrder(orderID)) != null) {
//			this.sentOrders.addOrder(data);
			this.addWrittenOrder(data);
			this.pendingSendOrders.removeOrder(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public IOrderData[] getAllCookingOrders() {
		return this.cookingOrders.getAllOrders();
	}

	@Override
	public IOrderData[] getAllPendingPaymentOrders() {
		return this.pendingPaymentOrders.getAllOrders();
	}

	@Override
	public IOrderData[] getAllPendingSendOrders() {
		return this.pendingSendOrders.getAllOrders();
	}

	@Override
	public IOrderData[] getAllSentOrders() {
		return this.sentOrders.getAllOrders();
	}

	@Override
	public IOrderData getCookingOrder(String orderID) {
		return this.cookingOrders.getOrder(orderID);
	}

	@Override
	public IOrderData getPendingPaymentOrder(String orderID) {
		return this.pendingPaymentOrders.getOrder(orderID);
	}

	@Override
	public IOrderData getPendingSendOrder(String orderID) {
		return this.pendingSendOrders.getOrder(orderID);
	}

	@Override
	public IOrderData getSentOrder(String orderID) {
		return this.sentOrders.getOrder(orderID);
	}
	
	@Override
	public void loadSaved() {
		this.getFileManager().loadSavedSettings();
		this.getFileManager().loadSavedDishMenu();
		this.getFileManager().loadSavedKnownDevices();
	}

	@Override
	public void editOrder(String orderID) {
		this.editOrderID = orderID;
		this.ordersChanged();
	}

	@Override
	public IOrderData getEditTarget() {
		return this.getOrder(this.editOrderID);
	}

	protected void clearEditTarget() {
		this.editOrderID = null;
	}

	@Override
	public void orderSent(String orderID) {
		this.makeSentOrder(orderID);
	}

	@Override
	public IOrderData[] getAllWrittenOrders() {
		return this.sentOrders.getAllOrders();
	}

	@Override
	protected void addWrittenOrder(IOrderData data) {
		this.sentOrders.addOrder(data);
	}
}
