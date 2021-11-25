package client.model;

import model.Model;
import model.order.IOrderData;
import model.order.OrderStatus;

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
//		IOrderData data = null;
//		if ((data = this.getCookingOrder(id)) != null) {
//			return data;
//		}
//		if ((data = this.getPendingPaymentOrder(id)) != null) {
//			return data;
//		}
//		if ((data = this.getPendingSendOrder(id)) != null) {
//			return data;
//		}
//		if ((data = this.getSentOrder(id)) != null) {
//			return data;
//		}
		return this.getOrderCollector().getOrder(id);
	}

	@Override
	public void removeAllOrders() {
//		this.cookingOrders.clearOrders();
//		this.pendingPaymentOrders.clearOrders();
//		this.pendingSendOrders.clearOrders();
//		this.sentOrders.clearOrders();
		this.getOrderCollector().clearOrders();
		this.ordersChanged();
	}

	@Override
	public void removeOrder(String id) {
//		this.cookingOrders.removeOrder(id);
//		this.pendingPaymentOrders.removeOrder(id);
//		this.pendingSendOrders.removeOrder(id);
//		this.sentOrders.removeOrder(id);
		this.getOrderCollector().removeOrder(id);
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
//			this.cookingOrders.addOrder(data);
			this.getOrderCollector().addOrder(data, OrderStatus.COOKING);
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
			this.getOrderCollector().editOrderStatus(orderID, OrderStatus.PENDING_PAYMENT);
			this.ordersChanged();
		}
	}

	@Override
	public void makePendingSendOrder(String formerID, String serialisedOrderData) {
		IOrderData data = this.getOrderHelper().deserialiseOrderData(serialisedOrderData);
		if (data != null && this.getPendingPaymentOrder(formerID) != null) {
//			this.pendingSendOrders.addOrder(data);
			if (!formerID.equals(data.getID().toString())) {
				this.removeOrder(formerID);
			}
//			this.pendingPaymentOrders.removeOrder(data.getID().toString());
			this.getOrderCollector().addOrder(data, OrderStatus.PENDING_SEND);
			this.writeOrder(data.getID().toString());
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
			this.getOrderCollector().editOrderStatus(orderID, OrderStatus.SENT);
			this.ordersChanged();
		}
	}

	@Override
	public IOrderData[] getAllCookingOrders() {
//		return this.cookingOrders.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithStatus(OrderStatus.COOKING);
	}

	@Override
	public IOrderData[] getAllPendingPaymentOrders() {
//		return this.pendingPaymentOrders.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithStatus(OrderStatus.PENDING_PAYMENT);
	}

	@Override
	public IOrderData[] getAllPendingSendOrders() {
//		return this.pendingSendOrders.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithStatus(OrderStatus.PENDING_SEND);
	}

	@Override
	public IOrderData[] getAllSentOrders() {
//		return this.sentOrders.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithStatus(OrderStatus.SENT);
	}

	@Override
	public IOrderData getCookingOrder(String orderID) {
//		return this.cookingOrders.getOrder(orderID);
		return this.getOrderCollector().getOrderIfStatusEqual(orderID, OrderStatus.COOKING);
	}

	@Override
	public IOrderData getPendingPaymentOrder(String orderID) {
//		return this.pendingPaymentOrders.getOrder(orderID);
		return this.getOrderCollector().getOrderIfStatusEqual(orderID, OrderStatus.PENDING_PAYMENT);
	}

	@Override
	public IOrderData getPendingSendOrder(String orderID) {
//		return this.pendingSendOrders.getOrder(orderID);
		return this.getOrderCollector().getOrderIfStatusEqual(orderID, OrderStatus.PENDING_SEND);
	}

	@Override
	public IOrderData getSentOrder(String orderID) {
//		return this.sentOrders.getOrder(orderID);
		return this.getOrderCollector().getOrderIfStatusEqual(orderID, OrderStatus.SENT);
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
}
