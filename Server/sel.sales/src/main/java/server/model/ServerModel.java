package server.model;

import model.Model;
import model.datamapper.order.OrderAttribute;
import model.dish.DishMenuItemData;
import model.entity.id.EntityID;
import model.order.OrderData;
import model.order.OrderStatus;

public class ServerModel extends Model implements IServerModel {
	
	private volatile boolean autoConfirmOrders = false;
	
	public ServerModel() {
		super();
	}
	
	public ServerModel(String resourceFolder) {
		this();
		this.getFileManager().setResourcesFolderAddress(resourceFolder);
	}
	
//	protected void makeUnconfirmedOrder(String id) {
//		this.getOrderCollector().editOrderStatus(id, OrderStatus.UNCONFIRMED);
//	}
//	
//	protected void makeConfirmedOrder(String id) {
//		this.getOrderCollector().editOrderStatus(id, OrderStatus.CONFIRMED);
//	}
//	
//	protected void makePastOrder(String id) {
//		this.getOrderCollector().editOrderStatus(id, OrderStatus.PAST);
//	}
	
//	protected void ordersChanged() {
//		this.unconfirmedOrdersChanged();
//		this.confirmedOrdersChanged();
//	}
	
	@Override
	public void addOrder(OrderData data) {
		this.addUnconfirmedOrder(data);
	}
	
	protected void unconfirmedOrdersChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshUnconfirmedOrders());
//		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshUnconfirmedOrders());
	}
	
	protected void confirmedOrdersChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshConfirmedOrders());
//		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshConfirmedOrders());
	}
	
	protected void orderConfirmModeChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshConfirmMode());
//		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshConfirmMode());
	}
	
	@Override
	public boolean writeOrders() {
		OrderData[] orders = this.getAllConfirmedOrders();
		
//		Collection<OrderData> ordersToBeWritten = new ArrayList<OrderData>();
//		for (OrderData od : orders) {
//			if (!this.isOrderWritten(od.getID().toString())) {
//				ordersToBeWritten.add(od);
//			}
//		}
//		boolean allWritten = true;
//		boolean current = false;
		for (OrderData od : orders) {
			this.writeOrder(od.getID().toString());
		}
		
//		OrderData[] array = ordersToBeWritten.toArray(OrderData[]::new);
//		boolean allWritten = this.getFileManager().writeOrderData(this.getOrderHelper().serialiseForFile(array));
//		if (allWritten) {
//			for (OrderData od : array) {
//				this.getOrderCollector().editWritten(od.getID().toString(), true);
////				this.writtenOrderCollector.addOrder(od);
//			}
//		}
		
		return true;
	}
	
	public void addMenuItem(String serialisedItemData) {
		this.addMenuItem(this.getDishMenuItemDAO().parseValueObject(serialisedItemData));
	}
	
	public void removeMenuItem(EntityID id) {
		if (this.getDishMenu().removeElement(id) != null) {
			this.menuChanged();
		}
	}

	public void removeMenuItem(String id) {
		this.removeMenuItem(this.createMinimalID(id));
	}

	@Override
	public void addUnconfirmedOrder(OrderData orderData) {
		if (this.autoConfirmOrders) {
			this.confirmOrder(orderData);
		} else {
			this.getOrderCollector().addElement(orderData);
			this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, orderData.getID(), OrderStatus.UNCONFIRMED);
			this.unconfirmedOrdersChanged();
		}
	}

	@Override
	public OrderData[] getAllUnconfirmedOrders() {
//		return this.orderUnconfirmedCollector.getAllOrders();
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED)).toArray(OrderData[]::new);
	}

	@Override
	public void removeAllUnconfirmedOrders() {
		this.getOrderCollector().removeAllElementsWithAttributeValue(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED);
//		this.orderUnconfirmedCollector.clearOrders();
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void editMenuItem(DishMenuItemData data) {
		if (data != null) {
			this.getDishMenu().removeElement(data.getID());
			this.getDishMenu().addElement(data);
			this.menuChanged();
		}
	}
	
	@Override
	public void confirmOrder(OrderData data) {
		this.getOrderCollector().addElement(data);
		this.getOrderCollector().setAttributeValue(OrderAttribute.STATUS, data.getID(), OrderStatus.CONFIRMED);
		this.writeOrder(data.getID().toString());
		this.ordersChanged();
	}

	@Override
	public OrderData[] getAllConfirmedOrders() {
		return this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.CONFIRMED)).toArray(OrderData[]::new);
//		return this.orderConfirmedCollector.getAllOrders();
	}
	
	public void removeUnconfirmedOrder(EntityID id) {
//		this.orderUnconfirmedCollector.removeOrder(id);
		this.getOrderCollector().removeElementIfAttributeValueEquals(OrderAttribute.STATUS, id, OrderStatus.UNCONFIRMED);
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void removeUnconfirmedOrder(String id) {
		this.removeUnconfirmedOrder(this.createMinimalID(id));
	}
	
	public void removeConfirmedOrder(EntityID id) {
//		this.orderConfirmedCollector.removeOrder(id);
		this.getOrderCollector().removeElementIfAttributeValueEquals(OrderAttribute.STATUS, id, OrderStatus.CONFIRMED);
		this.confirmedOrdersChanged();
	}

	@Override
	public void removeConfirmedOrder(String id) {
		this.removeConfirmedOrder(this.createMinimalID(id));
	}

	@Override
	public void removeAllConfirmedOrders() {
//		this.orderConfirmedCollector.clearOrders();
		this.getOrderCollector().removeAllElementsWithAttributeValue(OrderAttribute.STATUS, OrderStatus.CONFIRMED);
		this.confirmedOrdersChanged();
	}

	@Override
	public boolean writeDishMenu() {
		return this.getFileManager().writeDishMenuData(this.getDishMenuItemDAO().serialiseValueObjects(this.getDishMenu().toData().getAllElements().toArray(DishMenuItemData[]::new)));
	}
	
	@Override
	public void confirmAllOrders() {
		OrderData[] unconfirmedOrders = this.getOrderCollector().toValueObjectArray(this.getOrderCollector().getAllElementsByAttributeValue(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED)).toArray(OrderData[]::new);
//		OrderData[] unconfirmedOrders = this.orderUnconfirmedCollector.getAllOrders();
		for (OrderData uco : unconfirmedOrders) {
			this.confirmOrder(uco);
		}
//		this.orderUnconfirmedCollector.clearOrders();
		this.ordersChanged();
	}

	@Override
	public void setAutoConfirmOrders(boolean autoConfirm) {
		this.autoConfirmOrders = autoConfirm;
		if (this.autoConfirmOrders) {
			this.confirmAllOrders();
		}
		this.orderConfirmModeChanged();
	}

	@Override
	public boolean getAutoConfirmOrders() {
		return this.autoConfirmOrders;
	}

	@Override
	public void loadDishMenu(String fileAddress) {
		this.getFileManager().loadDishMenu(fileAddress);
	}

	@Override
	public void addMenuItem(DishMenuItemData data) {
		this.getDishMenu().addElement(data);
		this.menuChanged();
	}
}
