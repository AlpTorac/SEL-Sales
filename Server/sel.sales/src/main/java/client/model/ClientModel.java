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
	private IOrderCollector pastOrders;
	
	public ClientModel() {
		super();
		
		this.cookingOrders = this.getOrderHelper().createOrderCollector();
		this.pendingPaymentOrders = this.getOrderHelper().createOrderCollector();
		this.pendingSendOrders = this.getOrderHelper().createOrderCollector();
		this.pastOrders = this.getOrderHelper().createOrderCollector();
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
		if ((data = this.getPastOrder(id)) != null) {
			return data;
		}
		return data;
	}

	@Override
	public void removeAllOrders() {
		this.cookingOrders.clearOrders();
		this.pendingPaymentOrders.clearOrders();
		this.pendingSendOrders.clearOrders();
		this.pastOrders.clearOrders();
		this.ordersChanged();
	}

	@Override
	public void removeOrder(String id) {
		this.cookingOrders.removeOrder(id);
		this.pendingPaymentOrders.removeOrder(id);
		this.pendingSendOrders.removeOrder(id);
		this.pastOrders.removeOrder(id);
		this.ordersChanged();
	}

	@Override
	public void addCookingOrder(String serialisedOrderData) {
		this.cookingOrders.addOrder(this.getOrderHelper().deserialiseOrderData(serialisedOrderData));
		this.ordersChanged();
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
	public void makePendingSendOrder(String orderID) {
		IOrderData data;
		if ((data = this.getPendingPaymentOrder(orderID)) != null) {
			if (this.isOrderWritten(orderID)) {
				this.writeOrder(orderID);
			}
			this.pendingSendOrders.addOrder(data);
			this.pendingPaymentOrders.removeOrder(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public void makePastOrder(String orderID) {
		IOrderData data;
		if ((data = this.getPendingSendOrder(orderID)) != null) {
			this.pastOrders.addOrder(data);
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
	public IOrderData[] getAllPastOrders() {
		return this.pastOrders.getAllOrders();
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
	public IOrderData getPastOrder(String orderID) {
		return this.pastOrders.getOrder(orderID);
	}

	@Override
	protected IOrderCollector getWrittenOrderCollector() {
		return this.pastOrders;
	}
}
