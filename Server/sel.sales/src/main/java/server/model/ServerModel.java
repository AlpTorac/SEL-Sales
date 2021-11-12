package server.model;

import java.util.ArrayList;
import java.util.Collection;

import model.Model;
import model.dish.IDishMenuItemData;
import model.order.IOrderCollector;
import model.order.IOrderData;

public class ServerModel extends Model implements IServerModel {
	
	private volatile boolean autoConfirmOrders = false;
	
	private IOrderCollector orderUnconfirmedCollector;
	private IOrderCollector orderConfirmedCollector;
	private IOrderCollector writtenOrderCollector;
	
	public ServerModel() {
		super();
		
		this.orderUnconfirmedCollector = this.getOrderHelper().createOrderCollector();
		this.orderConfirmedCollector = this.getOrderHelper().createOrderCollector();
		this.writtenOrderCollector = this.getOrderHelper().createOrderCollector();
	}
	
	public ServerModel(String resourceFolder) {
		this();
		this.getFileManager().setResourcesFolderAddress(resourceFolder);
	}
	
//	protected void ordersChanged() {
//		this.unconfirmedOrdersChanged();
//		this.confirmedOrdersChanged();
//	}
	
	private void unconfirmedOrdersChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshUnconfirmedOrders());
//		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshUnconfirmedOrders());
	}
	
	private void confirmedOrdersChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshConfirmedOrders());
//		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshConfirmedOrders());
	}
	
	private void orderConfirmModeChanged() {
		this.notifyUpdatableChange(u -> u instanceof OrderConfirmationStatusUpdatable,
				u -> ((OrderConfirmationStatusUpdatable) u).refreshConfirmMode());
//		this.updatables.stream().filter(u -> u instanceof OrderUpdatable).forEach(u -> ((OrderUpdatable) u).refreshConfirmMode());
	}
	
	@Override
	public void removeOrder(String id) {
		this.removeConfirmedOrder(id);
		this.removeUnconfirmedOrder(id);
		this.ordersChanged();
	}
	
	@Override
	public boolean writeOrders() {
		IOrderData[] orders = this.getAllConfirmedOrders();
		Collection<IOrderData> ordersToBeWritten = new ArrayList<IOrderData>();
		for (IOrderData od : orders) {
			if (!this.isOrderWritten(od.getID().toString())) {
				ordersToBeWritten.add(od);
			}
		}
		IOrderData[] array = ordersToBeWritten.toArray(IOrderData[]::new);
		boolean allWritten = this.getFileManager().writeOrderData(this.getOrderHelper().serialiseForFile(array));
		if (allWritten) {
			for (IOrderData od : array) {
				this.getWrittenOrderCollector().addOrder(od);
			}
		}
		return allWritten;
	}
	
	@Override
	public IOrderData getOrder(String id) {
		IOrderData order = this.orderUnconfirmedCollector.getOrder(id);
		
		if (order == null) {
			order = this.orderConfirmedCollector.getOrder(id);
		}
		
		return order;
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
		this.orderUnconfirmedCollector.addOrder(order);
		if (this.autoConfirmOrders) {
			this.confirmOrder(orderData);
		} else {
			this.unconfirmedOrdersChanged();
		}
	}

	@Override
	public IOrderData[] getAllUnconfirmedOrders() {
		return this.orderUnconfirmedCollector.getAllOrders();
	}

	@Override
	public void removeAllUnconfirmedOrders() {
		this.orderUnconfirmedCollector.clearOrders();
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void editMenuItem(String serialisedNewItemData) {
		IDishMenuItemData data = this.getDishMenuHelper().deserialiseDishMenuItem(serialisedNewItemData);
		this.getDishMenu().editMenuItem(data);
		this.menuChanged();
	}

	protected void confirmOrder(IOrderData orderData) {
		this.orderUnconfirmedCollector.removeOrder(orderData.getID().toString());
		this.orderConfirmedCollector.addOrder(orderData);
		if (!this.isOrderWritten(orderData.getID().toString())) {
			this.getFileManager().writeOrderData(this.getOrderHelper().serialiseForFile(orderData));
			this.getWrittenOrderCollector().addOrder(orderData);
		}
		this.ordersChanged();
	}
	
	@Override
	public void confirmOrder(String serialisedConfirmedOrderData) {
		IOrderData orderData = this.getOrderHelper().deserialiseOrderData(serialisedConfirmedOrderData);
		this.confirmOrder(orderData);
	}

	@Override
	public IOrderData[] getAllConfirmedOrders() {
		return this.orderConfirmedCollector.getAllOrders();
	}

	@Override
	public void removeUnconfirmedOrder(String id) {
		this.orderUnconfirmedCollector.removeOrder(id);
		this.unconfirmedOrdersChanged();
	}

	@Override
	public void removeConfirmedOrder(String id) {
		this.orderConfirmedCollector.removeOrder(id);
		this.confirmedOrdersChanged();
	}

	@Override
	public void removeAllConfirmedOrders() {
		this.orderConfirmedCollector.clearOrders();
		this.confirmedOrdersChanged();
	}

	@Override
	public boolean writeDishMenu() {
		return this.getFileManager().writeDishMenuData(this.getDishMenuHelper().serialiseMenuForFile(this.getDishMenuHelper().dishMenuToData(this.getDishMenu())));
	}
	
	@Override
	public void confirmAllOrders() {
		IOrderData[] unconfirmedOrders = this.orderUnconfirmedCollector.getAllOrders();
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

	@Override
	protected IOrderCollector getWrittenOrderCollector() {
		return this.writtenOrderCollector;
	}
}
