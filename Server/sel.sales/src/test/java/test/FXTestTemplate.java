package test;

import java.io.Closeable;
import java.io.File;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Assertions;
import org.testfx.framework.junit5.ApplicationTest;

import client.controller.IClientController;
import client.controller.StandardClientController;
import client.model.ClientModel;
import client.model.IClientModel;
import client.view.IClientView;
import client.view.StandardClientView;
import javafx.application.Platform;
import model.IModel;
import model.connectivity.IConnectivityManager;
import model.datamapper.menu.DishMenuItemDAO;
import model.datamapper.order.OrderDAO;
import model.dish.DishMenu;
import model.dish.DishMenuItem;
import model.dish.DishMenuItemData;
import model.dish.DishMenuItemFactory;
import model.entity.AccumulatingAggregateEntry;
import model.order.OrderData;
import model.settings.SettingsField;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.StandardServerView;
import test.client.view.StandardClientViewOperationsUtilityClass;
import test.external.dummy.DummyClientExternal;
import test.model.dish.DishMenuItemTestUtilityClass;
import view.repository.uifx.FXUIComponentFactory;

public abstract class FXTestTemplate extends ApplicationTest {
	protected final long waitTime = 100;
	
	protected final String i1Name = "aaa";
	protected final BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	protected final BigDecimal i1Price = BigDecimal.valueOf(5);
	protected final BigDecimal i1ProCost = BigDecimal.valueOf(4);
	protected final BigDecimal i1Disc = BigDecimal.valueOf(0);
	protected final String i1id = "item1";
	
	protected final String i2Name = "bbb";
	protected final BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	protected final BigDecimal i2Price = BigDecimal.valueOf(1);
	protected final BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	protected final BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	protected final String i2id = "item2";
	
	protected final String i3Name = "ccc";
	protected final BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	protected final BigDecimal i3Price = BigDecimal.valueOf(4);
	protected final BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	protected final BigDecimal i3Disc = BigDecimal.valueOf(1);
	protected final String i3id = "item3";
	
	protected final String discName = "disc";
	protected final BigDecimal discPorSize = BigDecimal.ONE;
	protected final BigDecimal discPrice = BigDecimal.valueOf(-1);
	protected final BigDecimal discProCost = BigDecimal.ONE;
	protected final String discID = "discID";
	
	protected final String Device1Name = "c1n";
	protected final String Device1Address = "c1a";
	protected final String Device2Name = "c2n";
	protected final String Device2Address = "c2a";
	
	protected final BigDecimal o1a1 = BigDecimal.valueOf(2);
	
	protected final BigDecimal o2a1 = BigDecimal.valueOf(2);
	protected final BigDecimal o2a2 = BigDecimal.valueOf(3);
	protected final BigDecimal o2ad = BigDecimal.valueOf(0.3);
	
	protected final BigDecimal o3a3 = BigDecimal.valueOf(5);
	protected final BigDecimal o3ad = BigDecimal.valueOf(5);
	
	protected DishMenuItemData iData1;
	protected DishMenuItemData iData2;
	protected DishMenuItemData iData3;
	protected DishMenuItemData discData;
	
	protected DishMenuItem i1;
	protected DishMenuItem i2;
	protected DishMenuItem i3;
	protected DishMenuItem disc;
	
	protected IConnectivityManager connManager;
	protected DishMenuItemDAO menuDAO;
	protected OrderDAO orderDAO;
	
	protected final String o1id = "order1";
	protected final String o2id = "order2";
	protected final String o3id = "order3";
	
	protected OrderData oData1;
	protected OrderData oData2;
	protected OrderData oData3;
	
	protected AccumulatingAggregateEntry<DishMenuItemData> orderItem1;
	protected AccumulatingAggregateEntry<DishMenuItemData> orderItem2;
	protected AccumulatingAggregateEntry<DishMenuItemData> orderItem3;
	
	protected final String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
	protected final String serviceID = "serviceID";
	protected final String serviceName = "serviceName";
	
	protected final String clientAddress = "clientAddress";
	protected final String clientName = "clientName";
	protected final String serverAddress = "serverAddress";
	protected final String serverName = "serverName";
	
	protected final String deviceName = "deviceName";
	protected final String deviceAddress = "deviceAddress";
	
	protected final String serialisedTableNumbers = "1-2,4,5,1-10,90,11";
	protected final String serialisedTableNumbers2 = "1-5";
	
	protected StandardClientViewOperationsUtilityClass clientOpHelper;
	protected StandardServerViewOperationsUtilityClass serverOpHelper;
	
	protected DishMenu menu;
	
	protected volatile boolean actionFinished = false;
	
	protected void waitForAction() {
		while (!actionFinished) {
			
		}
		actionFinished = false;
	}
	
	protected void runFXAction(Runnable run) {
		Platform.runLater(() -> {
			run.run();
			actionFinished = true;
		});
		waitForAction();
	}
	
	public void initDishMenu() {
		menu = new DishMenu();
		menu.addElement(iData1);
		menu.addElement(iData2);
		menu.addElement(iData3);
	}
	
	public static boolean ordersEqualStatic(OrderData od1, OrderData od2) {
		return GeneralTestUtilityClass.arrayContentEquals(od1.getOrderedItems(), od2.getOrderedItems());
	}
	
	public boolean ordersEqual(OrderData od1, OrderData od2) {
		return GeneralTestUtilityClass.arrayContentEquals(od1.getOrderedItems(), od2.getOrderedItems());
	}
	
	public void setServerOpHelper(IServerModel serverModel, IServerController serverController, StandardServerView serverView) {
		serverOpHelper = new StandardServerViewOperationsUtilityClass(serverView, serverController, serverModel);
	}
	
	public void setClientOpHelper(IClientModel clientModel, IClientController clientController, IClientView clientView) {
		clientOpHelper = new StandardClientViewOperationsUtilityClass(
				clientView, clientController, clientModel);
	}
	
	public DummyClientExternal initClientExternal(String serviceID, String serviceName, IClientModel model, IClientController controller) {
		return new DummyClientExternal(serviceID, serviceName, controller, model);
	}
	
	public IClientView initClientView(IClientModel model, IClientController controller) {
		return new StandardClientView(new FXUIComponentFactory(), controller, model);
	}
	
	public IServerController initServerController(IServerModel model) {
		return new StandardServerController(model);
	}
	
	public StandardServerView initServerView(IServerModel model, IServerController controller) {
		return new StandardServerView(new FXUIComponentFactory(), controller, model);
	}
	
	public void getPrivateFieldsFromModel(IModel model) {
		menuDAO = GeneralTestUtilityClass.getPrivateFieldValue(model, "menuDAO");
		orderDAO = GeneralTestUtilityClass.getPrivateFieldValue(model, "orderDAO");
		connManager = GeneralTestUtilityClass.getPrivateFieldValue(model, "connManager");
		model.addSetting(SettingsField.DISH_MENU_FOLDER, this.testFolderAddress);
		model.addSetting(SettingsField.ORDER_FOLDER, this.testFolderAddress);
	}
	
	public IClientModel initClientModel() {
		IClientModel model = new ClientModel(this.testFolderAddress);
		this.getPrivateFieldsFromModel(model);
		return model;
	}
	
	public IClientController initClientController(IClientModel model) {
		return new StandardClientController(model);
	}
	
	public IServerModel initServerModel() {
		IServerModel model = new ServerModel(this.testFolderAddress);
		this.getPrivateFieldsFromModel(model);
		return model;
	}
	
	public void confirmOrder(IServerModel model, String id) {
		model.confirmOrder(model.getOrder(id));
	}
	
	public void getServerMenuIntoClient(IServerModel serverModel, IClientModel clientModel) {
		clientModel.setDishMenu(serverModel.getMenuData());
	}
	
	public void getServerMenuIntoClient(IServerModel serverModel, IClientModel clientModel, IClientView clientView) {
		this.getServerMenuIntoClient(serverModel, clientModel);
		clientView.refreshMenu();
	}
	
	public String serialiseDate(String date, IModel model) {
//		String year = date.substring(0, 4);
//		String month = date.substring(4, 6);
//		String day = date.substring(6,8);
//		String hour = date.substring(8, 10);
//		String min = date.substring(10, 12);
//		String sec = date.substring(12, 14);
//		String ms = date.substring(14, 17);
//		return day+"/"+month+"/"+year+hour+":"+min+":"+sec+":"+ms;
		return model.getDateSettings().serialiseDateWithoutSeparators(model.getDateSettings().parseDateWithoutSeparators(date));
	}
	
	public ExecutorService initExecutorService() {
		return Executors.newCachedThreadPool();
	}
	
	public void initOrders(IModel model) {
		oData1 = model.getOrderFactory().constructData(o1id, model.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809112233343",model)), false, false);
		oData1.addOrderItem(iData1, o1a1);
		
		oData2 = model.getOrderFactory().constructData(o2id, model.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809235959111", model)), true, false);
		oData2.addOrderItem(iData1, o2a1);
		oData2.addOrderItem(iData2, o2a2);
		
		oData3 = model.getOrderFactory().constructData(o3id, model.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809000000222", model)), true, true);
		oData3.addOrderItem(iData3, o3a3);
		
		orderItem1 = oData1.getOrderedItem(iData1.getID());
		orderItem2 = oData2.getOrderedItem(iData2.getID());
		orderItem3 = oData3.getOrderedItem(iData3.getID());
	}
	
	public void addOrdersToModel(IModel model) {
		model.addOrder(oData1);
		model.addOrder(oData2);
		model.addOrder(oData3);
	}
	
	public void initDiscOrders(IServerModel model) {
		this.addDiscDishMenuToServerModel(model);
		
		oData1 = model.getOrderFactory().constructData(o1id, model.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809112233343",model)), false, false);
		oData1.addOrderItem(iData1, o1a1);
		
		oData2 = model.getOrderFactory().constructData(o2id, model.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809235959111",model)), true, false);
		oData2.addOrderItem(iData1, o2a1);
		oData2.addOrderItem(iData2, o2a2);
		oData2.addOrderItem(discData, o2ad);
		
		oData3 = model.getOrderFactory().constructData(o3id, model.getDateSettings()
				.parseDateWithoutSeparators(this.serialiseDate("20200809000000222",model)), true, true);
		oData3.addOrderItem(iData3, o3a3);
		oData3.addOrderItem(discData, o3ad);
		
		orderItem1 = oData1.getOrderedItem(iData1.getID());
		orderItem2 = oData2.getOrderedItem(iData2.getID());
		orderItem3 = oData3.getOrderedItem(iData3.getID());
	}
	
	public void initDishMenuItems(IModel model) {
		DishMenuItemFactory fac = model.getMenuItemFactory();
		
		iData1 = fac.constructData(i1Name, i1id, i1PorSize, i1Price, i1ProCost);
		iData2 = fac.constructData(i2Name, i2id, i2PorSize, i2Price, i2ProCost);
		iData3 = fac.constructData(i3Name, i3id, i3PorSize, i3Price, i3ProCost);
		
		i1 = fac.valueToEntity(iData1);
		i2 = fac.valueToEntity(iData2);
		i3 = fac.valueToEntity(iData3);
	}
	
	public void addDishMenuToServerModel(IServerModel model) {
		this.initDishMenuItems(model);
		model.addMenuItem(iData1);
		model.addMenuItem(iData2);
		model.addMenuItem(iData3);
		
//		this.addMenuItemToServerModel(model, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
//		this.addMenuItemToServerModel(model, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
//		this.addMenuItemToServerModel(model, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
//		
//		iData1 = model.getMenuItem(i1id);
//		iData2 = model.getMenuItem(i2id);
//		iData3 = model.getMenuItem(i3id);
//		
//		i1 = iData1.getAssociatedItem(model.getActiveDishMenuItemFinder());
//		i2 = iData2.getAssociatedItem(model.getActiveDishMenuItemFinder());
//		i3 = iData3.getAssociatedItem(model.getActiveDishMenuItemFinder());
	}
	
	public void addDiscDishMenuToServerModel(IServerModel model) {
		this.addDishMenuToServerModel(model);
		this.addMenuItemToServerModel(model, discName, discID, discPorSize, discPrice, discProCost);
		discData = model.getMenuItem(discID);
		disc = discData.getAssociatedItem(model.getActiveDishMenuItemFinder());
	}
	
	public IServerModel createMinimalServerModel() {
		return new ServerModel(this.testFolderAddress);
	}
	
	public void addMenuItemToServerModel(IServerModel model, String dishName, String id, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost) {
		model.addMenuItem(model.getMenuItemFactory().constructData(dishName, id, portionSize, price, productionCost));
	}
	
	public String serialiseMenuItem(IServerModel model, String dishName, String id, BigDecimal portionSize, BigDecimal price,
			BigDecimal productionCost) {
		return model.serialiseMenuItem(model.getMenuItemFactory().constructData(dishName, id, portionSize, price, productionCost));
	}
	
	public void dishMenuItemDataAssertion(DishMenuItemData[] datas) {
		for (DishMenuItemData data : datas) {
			if (data.getID().toString().equals(i1id)) {
				DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data, i1Name, i1id, i1PorSize, i1Price, i1ProCost);
			} else if (data.getID().toString().equals(i2id)) {
				DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data, i2Name, i2id, i2PorSize, i2Price, i2ProCost);
			} else if (data.getID().toString().equals(i3id)) {
				DishMenuItemTestUtilityClass.assertMenuItemDataEqual(data, i3Name, i3id, i3PorSize, i3Price, i3ProCost);
			} else {
				Assertions.fail("An id has been parsed wrong");
			}
		}
	}
	
	public void cleanTestFolder() {
		GeneralTestUtilityClass.deletePathContent(this.testFolderAddress);
	}
	
	public void closeAll(Closeable... closeables) {
		for (Closeable c : closeables) {
			if (c instanceof IModel) {
				this.closeModel((IModel) c);
			} else {
				try {
					c.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
		this.cleanTestFolder();
	}
	
	public void closeModel(IModel model) {
		model.close();
		this.cleanTestFolder();
	}
	
	public void closeExecutorService(ExecutorService es) {
		try {
			es.awaitTermination(100, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
}
