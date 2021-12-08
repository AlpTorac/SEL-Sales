package client.model;

import model.Model;
import model.datamapper.order.OrderAttribute;
import model.entity.id.EntityID;
import model.order.Order;
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
	public void addOrder(OrderData data) {
		this.addCookingOrder(data);
	}
	
	public void addCookingOrder(String serialisedOrder) {
		this.addCookingOrder(this.getOrderDAO().parseValueObject(serialisedOrder));
	}

	@Override
	public void addCookingOrder(OrderData data) {
		EntityID orderID = data.getID();
		if (!this.getOrderCollector().contains(orderID) ||
				this.getOrderCollector().attributeValueEquals(OrderAttribute.STATUS, orderID, OrderStatus.EDITING)) {
			
			if (this.editOrderID != null && orderID.equals(this.editOrderID)) {
				this.getOrderCollector().getElement(orderID).setAttributesSameAs(data);
				this.clearEditTarget();
			} else {
				this.getOrderCollector().addElement(data);
			}
			
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderID, OrderStatus.COOKING);
			this.writeOrder(orderID);
			this.ordersChanged();
		}
	}
	
	public void makePendingPaymentOrder(EntityID orderID) {
		if (this.getCookingOrder(orderID) != null) {
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderID, OrderStatus.PENDING_PAYMENT);
			this.writeOrder(orderID);
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
			this.getOrderCollector().getElement(orderID).setAttributesSameAs(data);
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderID, OrderStatus.PENDING_SEND);
			this.writeOrder(orderID);
			this.ordersChanged();
		}
	}
	
	public void makeSentOrder(EntityID orderID) {
		if (this.getPendingSendOrder(orderID) != null) {
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderID, OrderStatus.SENT);
			this.writeOrder(orderID);
			this.ordersChanged();
		}
	}

	@Override
	public void makeSentOrder(String orderID) {
		this.makeSentOrder(this.createMinimalID(orderID));
	}

	@Override
	public OrderData[] getAllCookingOrders() {
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.COOKING)).toArray(OrderData[]::new);
	}

	@Override
	public OrderData[] getAllPendingPaymentOrders() {
//		return this.pendingPaymentOrders.getAllOrders();
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.PENDING_PAYMENT)).toArray(OrderData[]::new);
	}

	@Override
	public OrderData[] getAllPendingSendOrders() {
//		return this.pendingSendOrders.getAllOrders();
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.PENDING_SEND)).toArray(OrderData[]::new);
	}

	@Override
	public OrderData[] getAllSentOrders() {
//		return this.sentOrders.getAllOrders();
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.SENT)).toArray(OrderData[]::new);
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
	
	public void editOrder(EntityID orderID) {
		if (orderID != null && this.getOrder(orderID) != null) {
			this.editOrderID = orderID;
//			this.getOrderCollector().editOrderStatus(editOrderID, OrderStatus.EDITING);
			System.out.println("Edit orderID: " + orderID + " , Status: " + this.getOrderCollector().getAttributeValue(OrderAttribute.STATUS, orderID));
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, this.editOrderID, OrderStatus.EDITING);
			System.out.println("Edit orderID: " + orderID + " , Status: " + this.getOrderCollector().getAttributeValue(OrderAttribute.STATUS, orderID));
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
		if (this.editOrderID != null) {
//			this.getOrderCollector().removeElement(this.editOrderID);
			this.editOrderID = null;
			this.ordersChanged();
		}
	}

	@Override
	public void orderSentByID(String orderID) {
		this.makeSentOrder(orderID);
	}
	
	@Override
	public void removeOrder(String orderID) {
		if (this.editOrderID != null && this.editOrderID.serialisedIDequals(orderID)) {
			this.clearEditTarget();
		} else {
			super.removeOrder(orderID);
		}
	}

	@Override
	public void orderSentBySerialisedVersion(String serialisedOrderData) {
		OrderData data = this.getOrderDAO().parseValueObject(serialisedOrderData);
		this.orderSentByID(data.getID().toString());
	}
}
