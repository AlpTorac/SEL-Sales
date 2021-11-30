package client.model;

import model.Model;
import model.order.IOrderData;
import model.order.OrderStatus;
import model.order.datamapper.OrderAttribute;

public class ClientModel extends Model implements IClientModel {
	private String editOrderID;
	
	public ClientModel() {
		super();
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
		return this.getOrderCollector().getOrder(id);
	}

	@Override
	public void removeAllOrders() {
		this.getOrderCollector().clearOrders();
		this.ordersChanged();
	}

	@Override
	public void addCookingOrder(String serialisedOrderData) {
		IOrderData data = this.getOrderHelper().deserialiseOrderData(serialisedOrderData);
		String orderID = data.getID().toString();
		if (!this.getOrderCollector().contains(orderID) ||
				this.getOrderCollector().orderAttributeEquals(orderID, OrderAttribute.STATUS, OrderStatus.EDITING)) {
//		if (this.getOrder(orderID) == null || this.getOrderCollector().orderStatusEquals(orderID, OrderStatus.EDITING)) {
//			if (this.editOrderID != null) {
//				this.removeOrder(editOrderID);
//				this.clearEditTarget();
//			}
			this.clearEditTarget();
//			this.getOrderCollector().addOrder(data, OrderStatus.COOKING);
			this.getOrderCollector().addOrder(data);
			this.getOrderCollector().setOrderAttribute(orderID, OrderAttribute.STATUS, OrderStatus.COOKING);
			this.writeOrder(orderID);
			this.orderStatusChanged(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public void makePendingPaymentOrder(String orderID) {
//		IOrderData data;
		if (this.getCookingOrder(orderID) != null) {
//		if ((data = this.getCookingOrder(orderID)) != null) {
//			this.pendingPaymentOrders.addOrder(data);
//			this.cookingOrders.removeOrder(orderID);
//			this.getOrderCollector().editOrderStatus(orderID, OrderStatus.PENDING_PAYMENT);
			this.getOrderCollector().setOrderAttribute(orderID, OrderAttribute.STATUS, OrderStatus.PENDING_PAYMENT);
			this.orderStatusChanged(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public void makePendingSendOrder(String serialisedOrderData) {
		IOrderData data = this.getOrderHelper().deserialiseOrderData(serialisedOrderData);
		String orderID = data.getID().toString();
		if (data != null && this.getPendingPaymentOrder(orderID) != null) {
//			this.pendingSendOrders.addOrder(data);
//			if (!formerID.equals(data.getID().toString())) {
//				this.removeOrder(formerID);
//			}
//			this.pendingPaymentOrders.removeOrder(data.getID().toString());
			this.getOrderCollector().addOrder(data);
			this.getOrderCollector().setOrderAttribute(orderID, OrderAttribute.STATUS, OrderStatus.PENDING_SEND);
			this.orderStatusChanged(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public void makeSentOrder(String orderID) {
//		IOrderData data;
//		if ((data = this.getPendingSendOrder(orderID)) != null) {
		if (this.getPendingSendOrder(orderID) != null) {
//			this.sentOrders.addOrder(data);
//			this.addWrittenOrder(data);
//			this.pendingSendOrders.removeOrder(orderID);
//			this.getOrderCollector().editOrderStatus(orderID, OrderStatus.SENT);
			this.getOrderCollector().setOrderAttribute(orderID, OrderAttribute.STATUS, OrderStatus.SENT);
			this.orderStatusChanged(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public IOrderData[] getAllCookingOrders() {
//		return this.cookingOrders.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.COOKING);
	}

	@Override
	public IOrderData[] getAllPendingPaymentOrders() {
//		return this.pendingPaymentOrders.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.PENDING_PAYMENT);
	}

	@Override
	public IOrderData[] getAllPendingSendOrders() {
//		return this.pendingSendOrders.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.PENDING_SEND);
	}

	@Override
	public IOrderData[] getAllSentOrders() {
//		return this.sentOrders.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.SENT);
	}

	@Override
	public IOrderData getCookingOrder(String orderID) {
//		return this.cookingOrders.getOrder(orderID);
		return this.getOrderCollector().getOrderIfAttributeEquals(orderID, OrderAttribute.STATUS, OrderStatus.COOKING);
	}

	@Override
	public IOrderData getPendingPaymentOrder(String orderID) {
//		return this.pendingPaymentOrders.getOrder(orderID);
		return this.getOrderCollector().getOrderIfAttributeEquals(orderID, OrderAttribute.STATUS, OrderStatus.PENDING_PAYMENT);
	}

	@Override
	public IOrderData getPendingSendOrder(String orderID) {
//		return this.pendingSendOrders.getOrder(orderID);
		return this.getOrderCollector().getOrderIfAttributeEquals(orderID, OrderAttribute.STATUS, OrderStatus.PENDING_SEND);
	}

	@Override
	public IOrderData getSentOrder(String orderID) {
//		return this.sentOrders.getOrder(orderID);
		return this.getOrderCollector().getOrderIfAttributeEquals(orderID, OrderAttribute.STATUS, OrderStatus.SENT);
	}
	
	@Override
	public void loadSaved() {
//		this.getFileManager().loadSavedSettings();
//		this.getFileManager().loadSavedDishMenu();
//		this.getFileManager().loadSavedKnownDevices();
		this.getFileManager().loadSaved();
		this.ordersChanged();
	}

	@Override
	public void editOrder(String orderID) {
		if (orderID != null && this.getOrder(orderID) != null) {
			this.editOrderID = orderID;
//			this.getOrderCollector().editOrderStatus(editOrderID, OrderStatus.EDITING);
			this.getOrderCollector().setOrderAttribute(this.editOrderID, OrderAttribute.STATUS, OrderStatus.EDITING);
			this.orderStatusChanged(this.editOrderID);
			this.ordersChanged();
		}
	}

	@Override
	public IOrderData getEditTarget() {
		return this.getOrder(this.editOrderID);
	}

	protected void clearEditTarget() {
		this.editOrderID = null;
		this.ordersChanged();
	}

	@Override
	public void orderSent(String orderID) {
		this.makeSentOrder(orderID);
	}
	
	@Override
	public void removeOrder(String orderID) {
		if (this.editOrderID != null && this.editOrderID.equals(orderID)) {
			this.clearEditTarget();
		}
		super.removeOrder(orderID);
	}
}
