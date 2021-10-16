package test.view;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import controller.IController;
import controller.MainController;
import javafx.application.Platform;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
import model.dish.serialise.IDishMenuItemSerialiser;
import model.order.IOrderData;
import test.GeneralTestUtilityClass;
import test.UIOperationsUtilityClass;
import test.model.order.OrderTestUtilityClass;
import view.IView;
import view.MainView;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;

@Execution(value = ExecutionMode.SAME_THREAD)
class OrderInspectionAreaTest extends ApplicationTest {
	private long waitTime = 100;
	
	private static IModel model;
	private static IController controller;
	private static IView view;
	private static IDishMenuItemSerialiser dishMenuItemSerialiser;
	
	private String i1Name = "aaa";
	private BigDecimal i1PorSize = BigDecimal.valueOf(2.34);
	private BigDecimal i1Price = BigDecimal.valueOf(5);
	private BigDecimal i1ProCost = BigDecimal.valueOf(4);
	private BigDecimal i1Disc = BigDecimal.valueOf(0);
	private String i1id = "item1";
	
	private String i2Name = "bbb";
	private BigDecimal i2PorSize = BigDecimal.valueOf(5.67);
	private BigDecimal i2Price = BigDecimal.valueOf(1);
	private BigDecimal i2ProCost = BigDecimal.valueOf(0.5);
	private BigDecimal i2Disc = BigDecimal.valueOf(0.1);
	private String i2id = "item2";
	
	private String i3Name = "ccc";
	private BigDecimal i3PorSize = BigDecimal.valueOf(3.34);
	private BigDecimal i3Price = BigDecimal.valueOf(4);
	private BigDecimal i3ProCost = BigDecimal.valueOf(3.5);
	private BigDecimal i3Disc = BigDecimal.valueOf(1);
	private String i3id = "item3";
	
	private boolean autoDone = false;
	
	@AfterEach
	void prep() {
		model.removeAllOrders();
	}
	
	@Override
	public void start(Stage stage) {
		model = new Model();
		controller = new MainController(model);
		view = new MainView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
		view.startUp();
		dishMenuItemSerialiser = model.getDishMenuItemSerialiser();
		model.addMenuItem(dishMenuItemSerialiser.serialise(i1Name, i1id, i1PorSize, i1ProCost, i1Price, i1Disc));
		model.addMenuItem(dishMenuItemSerialiser.serialise(i2Name, i2id, i2PorSize, i2ProCost, i2Price, i2Disc));
		model.addMenuItem(dishMenuItemSerialiser.serialise(i3Name, i3id, i3PorSize, i3ProCost, i3Price, i3Disc));
	}
	
	@Test
	void confirmOrderTest() {
		model.addUnconfirmedOrder("order2-20200809235959890-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		
		IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 1);
		IOrderData addedUnconfirmedOrder = unconfirmedOrders[0];
		
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
		IOrderData confirmedOrderData = opHelper.addConfirmOrder();
		
		OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, confirmedOrderData);
		
		IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 1);
		
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
	}
	
	@Test
	void removeUnconfirmedOrderTest() {
		model.addUnconfirmedOrder("order2-20200809235959890-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		
		IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 1);
		IOrderData addedUnconfirmedOrder = unconfirmedOrders[0];
		
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
		IOrderData removedOrderData = opHelper.removeUnconfirmedOrder();
		
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		
		IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 0);
		
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		
		OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, removedOrderData);
	}
	
	@Test
	void removeConfirmedOrderTest() {
		model.addUnconfirmedOrder("order2-20200809235959890-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		
		IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 1);
		IOrderData addedUnconfirmedOrder = unconfirmedOrders[0];
		
		IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 0);
		
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
		IOrderData confirmedOrderData = opHelper.addConfirmOrder();
		
		OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, confirmedOrderData);
		
		confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 1);
		
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		
		IOrderData removedConfirmedOrderData = opHelper.removeConfirmedOrder();
		
		confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 0);
		
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		
		OrderTestUtilityClass.assertOrderDatasEqual(removedConfirmedOrderData, confirmedOrderData);
	}
	
	@Test
	void confirmAllOrdersTest() {
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order6-20200813000000183-1-1:item3,5;item3,4;");
		model.addUnconfirmedOrder("order7-20200909112233937-0-0:item1,2;item2,5;");
		
		IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 3);
		
		IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 0);
		
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
		IOrderData[] unconfirmedOrderDatas = opHelper.getUnconfirmedOrders().toArray(IOrderData[]::new);
		IOrderData[] confirmedOrderDatas = opHelper.confirmAllOrders().toArray(IOrderData[]::new);
		
		confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 3);
		
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		
		for (IOrderData cod : confirmedOrderDatas) {
			boolean contains = false;
			for (IOrderData uod : unconfirmedOrderDatas) {
				contains = contains || uod.equals(cod);
			}
			Assertions.assertTrue(contains);
		}
	}
	
	@Test
	void confirmAllOrdersWithButtonTest() {
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order6-20200813000000183-1-1:item3,5;item3,4;");
		model.addUnconfirmedOrder("order7-20200909112233937-0-0:item1,2;item2,5;");
		
		IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 3);
		
		IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 0);
		
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
		IOrderData[] unconfirmedOrderDatas = opHelper.getUnconfirmedOrders().toArray(IOrderData[]::new);
		opHelper.confirmAllOrdersWithoutReturn();
		
		confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 3);
		
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		GeneralTestUtilityClass.performWait(waitTime);
		for (IOrderData cod : confirmedOrders) {
			boolean contains = false;
			for (IOrderData uod : unconfirmedOrderDatas) {
				contains = contains || cod.equals(uod);
			}
			Assertions.assertTrue(contains);
		}
	}
	
	@Test
	void confirmAllOrdersWithAutoTest() {		
		UIOperationsUtilityClass opHelper = new UIOperationsUtilityClass((MainView) view, controller, model);
		opHelper.toggleOnAutoConfirm();
		
		IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		
		IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 0);
		
		GeneralTestUtilityClass.performWait(waitTime);
		
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order6-20200813000000183-1-1:item3,5;item3,4;");
		model.addUnconfirmedOrder("order7-20200909112233937-0-0:item1,2;item2,5;");
		
		Platform.runLater(() -> {
			autoDone = true;
		});
		
		while (!autoDone) {
			
		}
		
		IOrderData[] co = model.getAllConfirmedOrders();
		Assertions.assertEquals(co.length, 3);
		
		IOrderData[] uo = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(uo.length, 0);
		
	}
}
