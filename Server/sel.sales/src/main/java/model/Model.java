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
import model.order.IOrderFactory;
import model.order.IOrderID;
import model.order.IOrderIDFactory;
import model.order.IOrderItemDataFactory;
import model.order.IOrderItemFactory;
import model.order.OrderCollector;
import model.order.OrderDataFactory;
import model.order.OrderFactory;
import model.order.OrderIDFactory;
import model.order.OrderItemDataFactory;
import model.order.OrderItemFactory;
import model.order.StandardOrderDeserialiser;

public class Model implements IModel {
	private Collection<Updatable> updatables;
	private IDishMenu dishMenu;
	private IDishMenuItemFactory fac;
	private IDishMenuItemDataFactory dataFac;
	private IDishMenuItemIDFactory idFac;
	
	private IOrderFactory orderFac;
	private IOrderItemFactory orderItemFac;
	private IOrderDataFactory orderDataFac;
	private IOrderItemDataFactory orderItemDataFac;
	private IOrderIDFactory orderIDFac;
	private IOrderCollector orderUnconfirmedCollector;
	private IOrderCollector orderConfirmedCollector;
	private IOrderDeserialiser orderDeserialiser;
	private IDishMenuItemFinder finder;
	
	public Model() {
		this.updatables = new ArrayList<Updatable>();
		this.dishMenu = new DishMenu();
		this.fac = new DishMenuItemFactory();
		this.dataFac = new DishMenuItemDataFactory();
		this.idFac = new DishMenuItemIDFactory();

		this.orderItemFac = new OrderItemFactory();
		this.orderDataFac = new OrderDataFactory();
		this.orderItemDataFac = new OrderItemDataFactory();
		this.orderUnconfirmedCollector = new OrderCollector();
		this.orderConfirmedCollector = new OrderCollector();
		this.orderIDFac = new OrderIDFactory();
		this.orderFac = new OrderFactory(this.orderItemFac);
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
	public void addUnconfirmedOrder(String orderData) {
		IOrder order = this.orderDeserialiser.deserialise(orderData);
		this.orderUnconfirmedCollector.addOrder(order);
		this.updatables.forEach(u -> u.refreshUnconfirmedOrders());
	}

	@Override
	public IOrderData getOrder(IOrderID id) {
		IOrder order = this.orderUnconfirmedCollector.getOrder(id);
		
		if (order == null) {
			order = this.orderConfirmedCollector.getOrder(id);
		}
		
		return order.getOrderData(this.orderDataFac, this.orderItemDataFac, this.dataFac);
	}

	@Override
	public IOrderData[] getAllUnconfirmedOrders() {
		IOrder[] orders = this.orderUnconfirmedCollector.getAllOrders();
		IOrderData[] data = new IOrderData[orders.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = this.orderDataFac.orderToData(orders[i], this.orderItemDataFac, this.dataFac);
		}
		return data;
	}

	@Override
	public void removeAllUnconfirmedOrders() {
		this.orderUnconfirmedCollector.clearOrders();
	}

	@Override
	public void editMenuItem(IDishMenuItemData newItem) {
		IDishMenuItem oldItem = this.getMenuItem(newItem.getId());
		
		oldItem.getDish().setName(newItem.getDishName());
		oldItem.setDiscount(newItem.getDiscount());
		oldItem.setPortionSize(newItem.getPortionSize());
		oldItem.setPrice(newItem.getGrossPrice());
		oldItem.setProductionCost(newItem.getProductionCost());
	}

	@Override
	public IOrderDataFactory getOrderDataCommunicationProtocoll() {
		return this.orderDataFac;
	}

	@Override
	public IOrderIDFactory getOrderItemDataCommunicationProtocoll() {
		return this.orderIDFac;
	}

	@Override
	public void addConfirmedOrder(IOrderData orderData) {
		this.orderUnconfirmedCollector.removeOrder(orderData.getID());
		IOrder confirmedOrder = this.orderFac.createOrder(this.finder, orderData);
		this.orderConfirmedCollector.addOrder(confirmedOrder);
		this.updatables.forEach(u -> u.refreshUnconfirmedOrders());
		this.updatables.forEach(u -> u.refreshConfirmedOrders());
	}

	@Override
	public IOrderData[] getAllConfirmedOrders() {
		IOrder[] orders = this.orderConfirmedCollector.getAllOrders();
		IOrderData[] data = new IOrderData[orders.length];
		for (int i = 0; i < data.length; i++) {
			data[i] = this.orderDataFac.orderToData(orders[i], this.orderItemDataFac, this.dataFac);
		}
		return data;
	}
}
