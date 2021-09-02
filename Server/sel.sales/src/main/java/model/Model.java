package model;

import java.util.ArrayList;
import java.util.Collection;

import model.dish.DishMenu;
import model.dish.IDishMenu;
import model.dish.IDishMenuItemData;
import model.dish.serialise.DishMenuItemSerialiser;
import model.dish.serialise.IDishMenuDeserialiser;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.dish.serialise.StandardDishMenuDeserialiser;
import model.order.IOrderCollector;
import model.order.IOrderData;
import model.order.OrderCollector;
import model.order.serialise.IOrderDeserialiser;
import model.order.serialise.IOrderSerialiser;
import model.order.serialise.OrderSerialiser;
import model.order.serialise.StandardOrderDeserialiser;

public class Model implements IModel {
	private Collection<Updatable> updatables;
	private IDishMenu dishMenu;
	
	private IOrderCollector orderUnconfirmedCollector;
	private IOrderCollector orderConfirmedCollector;
	private IOrderSerialiser orderSerialiser;
	private IOrderDeserialiser orderDeserialiser;
	private IDishMenuDeserialiser dishMenuDeserialiser;
	private IDishMenuItemFinder finder;
	private IDishMenuItemSerialiser menuItemSerialiser;
	
	public Model() {
		this.updatables = new ArrayList<Updatable>();
		this.dishMenu = new DishMenu();
		
		this.orderSerialiser = new OrderSerialiser();
		this.menuItemSerialiser = new DishMenuItemSerialiser();
		this.dishMenuDeserialiser = new StandardDishMenuDeserialiser();
		this.finder = new DishMenuItemFinder(this.dishMenu);
		this.orderUnconfirmedCollector = new OrderCollector();
		this.orderConfirmedCollector = new OrderCollector();
		this.orderDeserialiser = new StandardOrderDeserialiser(this.finder);
	}
	
	public void addMenuItem(String serialisedItemData) {
		IDishMenuItemData data = this.dishMenuDeserialiser.deserialise(serialisedItemData);
		if (this.dishMenu.addMenuItem(data)) {
			this.updatables.forEach(u -> u.refreshMenu());
		}
	}

	public void removeMenuItem(String id) {
		if (this.dishMenu.removeMenuItem(id)) {
			this.updatables.forEach(u -> u.refreshMenu());
		}
	}

	public IDishMenuItemData getMenuItem(String id) {
		return this.dishMenu.getItem(id);
	}

	@Override
	public IDishMenuItemData[] getMenuData() {
		return this.dishMenu.getAllItems();
	}

	@Override
	public void subscribe(Updatable updatable) {
		this.updatables.add(updatable);
	}

	@Override
	public void addUnconfirmedOrder(String orderData) {
		IOrderData order = this.orderDeserialiser.deserialise(orderData);
		this.orderUnconfirmedCollector.addOrder(order);
		this.updatables.forEach(u -> u.refreshUnconfirmedOrders());
	}

	@Override
	public IOrderData getOrder(String id) {
		IOrderData order = this.orderUnconfirmedCollector.getOrder(id);
		
		if (order == null) {
			order = this.orderConfirmedCollector.getOrder(id);
		}
		
		return order;
	}

	@Override
	public IOrderData[] getAllUnconfirmedOrders() {
		return this.orderUnconfirmedCollector.getAllOrders();
	}

	@Override
	public void removeAllUnconfirmedOrders() {
		this.orderUnconfirmedCollector.clearOrders();
	}

	@Override
	public void editMenuItem(String serialisedNewItemData) {
		IDishMenuItemData data = dishMenuDeserialiser.deserialise(serialisedNewItemData);
		this.dishMenu.editMenuItem(data);
		this.updatables.forEach(u -> u.refreshMenu());
	}

	@Override
	public void confirmOrder(String serialisedConfirmedOrderData) {
		IOrderData orderData = this.orderDeserialiser.deserialise(serialisedConfirmedOrderData);
		this.orderUnconfirmedCollector.removeOrder(orderData.getID());
		this.orderConfirmedCollector.addOrder(orderData);
		this.updatables.forEach(u -> u.refreshUnconfirmedOrders());
		this.updatables.forEach(u -> u.refreshConfirmedOrders());
	}

	@Override
	public IOrderData[] getAllConfirmedOrders() {
		return this.orderConfirmedCollector.getAllOrders();
	}

	@Override
	public void removeUnconfirmedOrder(String id) {
		this.orderUnconfirmedCollector.removeOrder(id);
		this.updatables.forEach(u -> u.refreshUnconfirmedOrders());
	}

	@Override
	public void removeConfirmedOrder(String id) {
		this.orderConfirmedCollector.removeOrder(id);
		this.updatables.forEach(u -> u.refreshConfirmedOrders());
	}

	@Override
	public IDishMenuItemSerialiser getDishMenuItemSerialiser() {
		return this.menuItemSerialiser;
	}

	@Override
	public IOrderSerialiser getOrderSerialiser() {
		return this.orderSerialiser;
	}
}
