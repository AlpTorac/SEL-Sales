package client.model;

import model.Model;
import model.datamapper.order.OrderAttribute;
import model.entity.id.EntityID;
import model.order.OrderData;
import model.order.OrderStatus;

public class ClientModel extends Model implements IClientModel {
	private EntityID editOrderID;
	
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
	public void removeAllOrders() {
		this.getOrderCollector().clearAllElements();
		this.ordersChanged();
	}

	@Override
	public void addCookingOrder(OrderData data) {
		EntityID orderID = data.getID();
		if (!this.getOrderCollector().contains(orderID) ||
				this.getOrderCollector().attributeValueEquals(OrderAttribute.STATUS, orderID, OrderStatus.EDITING)) {
//		if (this.getOrder(orderID) == null || this.getOrderCollector().orderStatusEquals(orderID, OrderStatus.EDITING)) {
//			if (this.editOrderID != null) {
//				this.removeOrder(editOrderID);
//				this.clearEditTarget();
//			}
			this.clearEditTarget();
//			this.getOrderCollector().addOrder(data, OrderStatus.COOKING);
			this.getOrderCollector().addElement(data);
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderID, OrderStatus.COOKING);
			this.writeOrder(orderID);
			this.orderStatusChanged(orderID);
			this.ordersChanged();
		}
	}
	
	public void makePendingPaymentOrder(EntityID orderID) {
//		OrderData data;
		if (this.getCookingOrder(orderID) != null) {
//		if ((data = this.getCookingOrder(orderID)) != null) {
//			this.pendingPaymentOrders.addOrder(data);
//			this.cookingOrders.removeOrder(orderID);
//			this.getOrderCollector().editOrderStatus(orderID, OrderStatus.PENDING_PAYMENT);
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderID, OrderStatus.PENDING_PAYMENT);
			this.orderStatusChanged(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public void makePendingPaymentOrder(String orderID) {
		this.makePendingPaymentOrder(this.createMinimalID(orderID));
	}

	@Override
	public void makePendingSendOrder(OrderData data) {
		System.out.println("pending send order isCash: " + data.getIsCash() + " , isHere: " + data.getIsHere());
		EntityID orderID = data.getID();
		if (data != null && this.getPendingPaymentOrder(orderID) != null) {
//			this.pendingSendOrders.addOrder(data);
//			if (!formerID.equals(data.getID().toString())) {
//				this.removeOrder(formerID);
//			}
//			this.pendingPaymentOrders.removeOrder(data.getID().toString());
			this.getOrderCollector().addElement(data);
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderID, OrderStatus.PENDING_SEND);
			this.orderStatusChanged(orderID);
			this.ordersChanged();
		}
	}
	
	public void makeSentOrder(EntityID orderID) {
		if (this.getPendingSendOrder(orderID) != null) {
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderID, OrderStatus.SENT);
			this.orderStatusChanged(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public void makeSentOrder(String orderID) {
		this.makeSentOrder(this.createMinimalID(orderID));
	}

	@Override
	public OrderData[] getAllCookingOrders() {
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.COOKING));
	}

	@Override
	public OrderData[] getAllPendingPaymentOrders() {
//		return this.pendingPaymentOrders.getAllOrders();
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.PENDING_PAYMENT));
	}

	@Override
	public OrderData[] getAllPendingSendOrders() {
//		return this.pendingSendOrders.getAllOrders();
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.PENDING_SEND));
	}

	@Override
	public OrderData[] getAllSentOrders() {
//		return this.sentOrders.getAllOrders();
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.SENT));
	}
	
	public OrderData getCookingOrder(EntityID orderID) {
//		return this.cookingOrders.getOrder(orderID);
		return this.getOrderCollector().toValueObject(this.getOrderCollector().getElementIfAttributeValueEquals(OrderAttribute.STATUS, orderID, OrderStatus.COOKING));
	}

	@Override
	public OrderData getCookingOrder(String orderID) {
		return this.getCookingOrder(this.createMinimalID(orderID));
	}
	
	public OrderData getPendingPaymentOrder(EntityID orderID) {
//		return this.pendingPaymentOrders.getOrder(orderID);
		return this.getOrderCollector().toValueObject(this.getOrderCollector().getElementIfAttributeValueEquals(OrderAttribute.STATUS, orderID, OrderStatus.PENDING_PAYMENT));
	}

	@Override
	public OrderData getPendingPaymentOrder(String orderID) {
		return this.getPendingPaymentOrder(this.createMinimalID(orderID));
	}
	
	public OrderData getPendingSendOrder(EntityID orderID) {
//		return this.pendingSendOrders.getOrder(orderID);
		return this.getOrderCollector().toValueObject(this.getOrderCollector().getElementIfAttributeValueEquals(OrderAttribute.STATUS, orderID, OrderStatus.PENDING_SEND));
	}

	@Override
	public OrderData getPendingSendOrder(String orderID) {
		return this.getPendingSendOrder(this.createMinimalID(orderID));
	}
	
	public OrderData getSentOrder(EntityID orderID) {
//		return this.sentOrders.getOrder(orderID);
		return this.getOrderCollector().toValueObject(this.getOrderCollector().getElementIfAttributeValueEquals(OrderAttribute.STATUS, orderID, OrderStatus.SENT));
	}

	@Override
	public OrderData getSentOrder(String orderID) {
		return this.getSentOrder(this.createMinimalID(orderID));
	}
	
	@Override
	public void loadSaved() {
//		this.getFileManager().loadSavedSettings();
//		this.getFileManager().loadSavedDishMenu();
//		this.getFileManager().loadSavedKnownDevices();
		this.getFileManager().loadSaved();
		this.ordersChanged();
	}
	
	public void editOrder(EntityID orderID) {
		if (orderID != null && this.getOrder(orderID) != null) {
			this.editOrderID = orderID;
//			this.getOrderCollector().editOrderStatus(editOrderID, OrderStatus.EDITING);
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, this.editOrderID, OrderStatus.EDITING);
			this.orderStatusChanged(this.editOrderID);
			this.ordersChanged();
		}
	}

	@Override
	public void editOrder(String orderID) {
		this.editOrder(this.createMinimalID(orderID));
	}

	@Override
	public OrderData getEditTarget() {
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
