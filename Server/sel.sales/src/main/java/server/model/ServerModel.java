package server.model;

import model.Model;
import model.dish.IDishMenuItemData;
import model.order.IOrderData;
import model.order.OrderStatus;
import model.order.datamapper.OrderAttribute;

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
	public void loadSaved() {
		this.getFileManager().loadSaved();
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
		IOrderData[] orders = this.getAllConfirmedOrders();
		
//		Collection<IOrderData> ordersToBeWritten = new ArrayList<IOrderData>();
//		for (IOrderData od : orders) {
//			if (!this.isOrderWritten(od.getID().toString())) {
//				ordersToBeWritten.add(od);
//			}
//		}
		boolean allWritten = true;
//		boolean current = false;
		for (IOrderData od : orders) {
			allWritten = allWritten && this.writeOrder(od.getID().toString());
		}
		
//		IOrderData[] array = ordersToBeWritten.toArray(IOrderData[]::new);
//		boolean allWritten = this.getFileManager().writeOrderData(this.getOrderHelper().serialiseForFile(array));
//		if (allWritten) {
//			for (IOrderData od : array) {
//				this.getOrderCollector().editWritten(od.getID().toString(), true);
////				this.writtenOrderCollector.addOrder(od);
//			}
//		}
		
		return allWritten;
	}
	
	@Override
	public IOrderData getOrder(String id) {
//		IOrderData order = this.orderUnconfirmedCollector.getOrder(id);
//		
//		if (order == null) {
//			order = this.orderConfirmedCollector.getOrder(id);
//		}
		return this.getOrderCollector().getOrder(id);
	}
	
	public void addMenuItem(String serialisedItemData) {
//		IDishMenuItemData data = this.dishMenuItemDeserialiser.deserialise(serialisedItemData);
		if (this.getDishMenu().addMenuItem(this.getDishMenuHelper().createDishMenuItem(serialisedItemData))) {
			this.menuChanged();
		}
	}

	public void removeMenuItem(String id) {
		if (this.getDishMenu().removeMenuItem(id)) {
			this.menuChanged();
		}
	}

	@Override
	public void addUnconfirmedOrder(String orderData) {
		IOrderData order = this.getOrderHelper().deserialiseOrderData(orderData);
//		this.orderUnconfirmedCollector.addOrder(order);
		if (this.autoConfirmOrders) {
			this.confirmOrder(order);
		} else {
//			this.makeUnconfirmedOrder(order.getID().toString());
//			this.getOrderCollector().addOrder(order, OrderStatus.UNCONFIRMED);
			this.getOrderCollector().addOrder(order);
			this.getOrderCollector().setOrderAttribute(order.getID().toString(), OrderAttribute.STATUS, OrderStatus.UNCONFIRMED);
			this.unconfirmedOrdersChanged();
		}
	}

	@Override
	public IOrderData[] getAllUnconfirmedOrders() {
//		return this.orderUnconfirmedCollector.getAllOrders();
		return this.getOrderCollector().getAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED);
	}

	@Override
	public void removeAllUnconfirmedOrders() {
		this.getOrderCollector().removeAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED);
//		this.orderUnconfirmedCollector.clearOrders();
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void editMenuItem(String serialisedNewItemData) {
		IDishMenuItemData data = this.getDishMenuHelper().deserialiseDishMenuItem(serialisedNewItemData);
		this.getDishMenu().editMenuItem(data);
		this.menuChanged();
	}
	
	protected void confirmOrder(IOrderData orderData) {
//		this.orderUnconfirmedCollector.removeOrder(orderData.getID().toString());
//		this.orderConfirmedCollector.addOrder(orderData);
//		this.getOrderCollector().addOrder(orderData, OrderStatus.CONFIRMED);
		this.getOrderCollector().addOrder(orderData);
		this.getOrderCollector().setOrderAttribute(orderData.getID().toString(), OrderAttribute.STATUS, OrderStatus.CONFIRMED);
		this.writeOrder(orderData.getID().toString());
		this.ordersChanged();
	}
	
	@Override
	public void confirmOrder(String serialisedConfirmedOrderData) {
		IOrderData orderData = this.getOrderHelper().deserialiseOrderData(serialisedConfirmedOrderData);
		this.confirmOrder(orderData);
	}

	@Override
	public IOrderData[] getAllConfirmedOrders() {
		return this.getOrderCollector().getAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.CONFIRMED);
//		return this.orderConfirmedCollector.getAllOrders();
	}

	@Override
	public void removeUnconfirmedOrder(String id) {
//		this.orderUnconfirmedCollector.removeOrder(id);
		this.getOrderCollector().removeOrderIfAttributeEquals(id, OrderAttribute.STATUS, OrderStatus.UNCONFIRMED);
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void removeConfirmedOrder(String id) {
//		this.orderConfirmedCollector.removeOrder(id);
		this.getOrderCollector().removeOrderIfAttributeEquals(id, OrderAttribute.STATUS, OrderStatus.CONFIRMED);
		this.confirmedOrdersChanged();
	}

	@Override
	public void removeAllConfirmedOrders() {
//		this.orderConfirmedCollector.clearOrders();
		this.getOrderCollector().removeAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.CONFIRMED);
		this.confirmedOrdersChanged();
	}

	@Override
	public boolean writeDishMenu() {
		return this.getFileManager().writeDishMenuData(this.getDishMenuHelper().serialiseMenuForFile(this.getDishMenuHelper().dishMenuToData(this.getDishMenu())));
	}
	
	@Override
	public void confirmAllOrders() {
		IOrderData[] unconfirmedOrders = this.getOrderCollector().getAllOrdersWithAttribute(OrderAttribute.STATUS, OrderStatus.UNCONFIRMED);
//		IOrderData[] unconfirmedOrders = this.orderUnconfirmedCollector.getAllOrders();
		for (IOrderData uco : unconfirmedOrders) {
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

//	@Override
//	protected IOrderCollector getWrittenOrderCollector() {
//		return this.writtenOrderCollector;
//	}
}
