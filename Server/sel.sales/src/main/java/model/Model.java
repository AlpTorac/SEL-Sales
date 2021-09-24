package model;

import java.util.ArrayList;
import java.util.Collection;

import model.dish.DishMenu;
import model.dish.DishMenuDataFactory;
import model.dish.DishMenuItemFinder;
import model.dish.IDishMenu;
import model.dish.IDishMenuData;
import model.dish.IDishMenuDataFactory;
import model.dish.IDishMenuItemData;
import model.dish.IDishMenuItemFinder;
import model.dish.serialise.IntraAppDishMenuItemSerialiser;
import model.dish.serialise.ExternalDishMenuSerialiser;
import model.dish.serialise.IDishMenuItemDeserialiser;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.dish.serialise.IDishMenuSerialiser;
import model.dish.serialise.StandardDishMenuDeserialiser;
import model.filewriter.DishMenuFileWriter;
import model.filewriter.OrderFileWriter;
import model.filewriter.StandardDishMenuFileWriter;
import model.filewriter.StandardOrderFileWriter;
import model.order.IOrderCollector;
import model.order.IOrderData;
import model.order.OrderCollector;
import model.order.serialise.IOrderDeserialiser;
import model.order.serialise.IOrderSerialiser;
import model.order.serialise.IntraAppOrderSerialiser;
import model.order.serialise.StandardOrderDeserialiser;

public class Model implements IModel {
	private Collection<Updatable> updatables;
	
	private IDishMenu dishMenu;
	private IDishMenuDataFactory dishMenuDataFac;
	
	private IOrderCollector orderUnconfirmedCollector;
	private IOrderCollector orderConfirmedCollector;
	private IOrderSerialiser orderSerialiser;
	private IOrderDeserialiser orderDeserialiser;
	
	private IDishMenuItemDeserialiser dishMenuItemDeserialiser;
	private IDishMenuItemFinder finder;
	private IDishMenuItemSerialiser menuItemSerialiser;
	
	/**
	 * Only for the external clients (outside the server part of the app)
	 */
	private IDishMenuSerialiser externalDishMenuSerialiser;
	
	private String orderFolderAddress;
	private OrderFileWriter orderWriter;
	
	private String dishMenuFolderAddress;
	private DishMenuFileWriter dishMenuWriter;

	public Model() {
		this.updatables = new ArrayList<Updatable>();
		
		this.dishMenu = new DishMenu();
		this.dishMenuDataFac = new DishMenuDataFactory();
		
		this.orderSerialiser = new IntraAppOrderSerialiser();
		this.menuItemSerialiser = new IntraAppDishMenuItemSerialiser();
		this.dishMenuItemDeserialiser = new StandardDishMenuDeserialiser();
		this.finder = new DishMenuItemFinder(this.dishMenu);
		this.orderUnconfirmedCollector = new OrderCollector();
		this.orderConfirmedCollector = new OrderCollector();
		this.orderDeserialiser = new StandardOrderDeserialiser(this.finder);
		
		this.orderFolderAddress = "src/main/resources/orders";
		this.orderWriter = new StandardOrderFileWriter(this.orderFolderAddress);
		
		this.dishMenuFolderAddress = "src/main/resources/dishMenuItems";
		this.dishMenuWriter = new StandardDishMenuFileWriter(this.dishMenuFolderAddress);
		
		this.externalDishMenuSerialiser = new ExternalDishMenuSerialiser();
	}
	
	public void addMenuItem(String serialisedItemData) {
		IDishMenuItemData data = this.dishMenuItemDeserialiser.deserialise(serialisedItemData);
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
	public IDishMenuData getMenuData() {
		return this.dishMenuDataFac.dishMenuToData(this.dishMenu);
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
		this.updatables.forEach(u -> u.refreshUnconfirmedOrders());
	}

	@Override
	public void editMenuItem(String serialisedNewItemData) {
		IDishMenuItemData data = dishMenuItemDeserialiser.deserialise(serialisedNewItemData);
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

	@Override
	public void removeAllConfirmedOrders() {
		this.orderConfirmedCollector.clearOrders();
		this.updatables.forEach(u -> u.refreshConfirmedOrders());
	}

	@Override
	public boolean writeOrders() {
		boolean b = true;
		for (IOrderData d : this.getAllConfirmedOrders()) {
			b = b && this.orderWriter.writeOrderData(d);
		}
		return b;
	}

	@Override
	public boolean writeDishMenu() {
		return this.dishMenuWriter.writeDishMenuData(this.dishMenuDataFac.dishMenuToData(this.dishMenu));
	}

	@Override
	public IDishMenuSerialiser getExternalDishMenuSerialiser() {
		return this.externalDishMenuSerialiser;
	}
}
