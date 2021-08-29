package model;

import java.util.ArrayList;
import java.util.Collection;

import model.dish.DishMenu;
import model.dish.DishMenuItemDataFactory;
import model.dish.DishMenuItemFactory;
import model.dish.DishMenuItemIDFactory;
import model.dish.IDishMenu;
import model.dish.IDishMenuItem;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemFactory;
import model.dish.IDishMenuItemID;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrder;
import model.order.IOrderCollector;
import model.order.IOrderData;
import model.order.IOrderDataFactory;
import model.order.IOrderDeserialiser;
import model.order.IOrderID;
import model.order.IOrderItemDataFactory;
import model.order.OrderCollector;
import model.order.OrderDataFactory;
import model.order.OrderItemDataFactory;
import model.order.StandardOrderDeserialiser;

public class Model implements IModel {
	private Collection<Updatable> updatables;
	private IDishMenu dishMenu;
	private IDishMenuItemFactory fac;
	private IDishMenuItemDataFactory dataFac;
	private IDishMenuItemIDFactory idFac;
	
	private IOrderDataFactory orderDataFac;
	private IOrderItemDataFactory orderItemDataFac;
	private IOrderCollector orderCollector;
	private IOrderDeserialiser orderDeserialiser;
	private IDishMenuItemFinder finder;
	
	public Model() {
		this.updatables = new ArrayList<Updatable>();
		this.dishMenu = new DishMenu();
		this.fac = new DishMenuItemFactory();
		this.dataFac = new DishMenuItemDataFactory();
		this.idFac = new DishMenuItemIDFactory();

		this.orderDataFac = new OrderDataFactory();
		this.orderItemDataFac = new OrderItemDataFactory();
		this.orderCollector = new OrderCollector();
		this.finder = new DishMenuItemFinder(this.dishMenu);
		this.orderDeserialiser = new StandardOrderDeserialiser(this.finder, this.dataFac, this.idFac);
	}
	
	public void addMenuItem(IDishMenuItemData item) {
		if (this.dishMenu.addMenuItem(this.fac.createMenuItem(item))) {
			this.updatables.forEach(u -> u.refreshMenu());
		}
	}

	public void removeMenuItem(IDishMenuItemID id) {
		if (this.dishMenu.removeMenuItem(id)) {
			this.updatables.forEach(u -> u.refreshMenu());
		}
	}

	public IDishMenuItem getMenuItem(IDishMenuItemID id) {
		return this.dishMenu.getItem(id);
	}

	@Override
	public IDishMenuItemDataFactory getItemDataCommunicationProtocoll() {
		return this.dataFac;
	}

	@Override
	public IDishMenuItemIDFactory getItemIDCommunicationProtocoll() {
		return this.idFac;
	}

	@Override
	public IDishMenuItemData[] getMenuData() {
		IDishMenuItem[] items = this.dishMenu.getAllItems();
		IDishMenuItemData[] data = new IDishMenuItemData[items.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = this.dataFac.menuItemToData(items[i]);
		}
		return data;
	}

	@Override
	public void subscribe(Updatable updatable) {
		this.updatables.add(updatable);
	}

	@Override
	public void addOrder(String orderData) {
		this.orderCollector.addOrder(this.orderDeserialiser.deserialise(orderData));
		this.updatables.forEach(u -> u.refreshOrders());
	}

	@Override
	public IOrderData getOrder(IOrderID id) {
		return this.orderCollector.getOrder(id).getOrderData(this.orderDataFac, this.orderItemDataFac, this.dataFac);
	}

	@Override
	public IOrderData[] getAllOrders() {
		IOrder[] orders = this.orderCollector.getAllOrders();
		IOrderData[] data = new IOrderData[orders.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = this.orderDataFac.orderToData(orders[i], this.orderItemDataFac, this.dataFac);
		}
		return data;
	}
}
