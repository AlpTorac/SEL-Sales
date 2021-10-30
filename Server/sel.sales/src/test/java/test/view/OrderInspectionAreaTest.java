package test.view;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Collection;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
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
import test.MainViewOperationsUtilityClass;
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
	
	private volatile boolean actionFinished = false;
	
	private void waitForAction() {
		while (!actionFinished) {
			
		}
		actionFinished = false;
	}
	
	private void runFXAction(Runnable run) {
		Platform.runLater(() -> {
			run.run();
			actionFinished = true;
		});
		waitForAction();
	}
	
	@BeforeEach
	void cleanUp() {
		runFXAction(()->{
			model = new Model();
			controller = new MainController(model);
			view = new MainView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
			view.startUp();
			view.show();
			model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
			model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
			model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
		});
	}
	
	@AfterEach
	void prep() {
		runFXAction(()->{
			model.removeAllOrders();
			view.hide();
			model.close();
		});
	}
	
	@Override
	public void start(Stage stage) {

	}
	
	@Test
	void confirmOrderTest() {
		runFXAction(()->{
			model.addUnconfirmedOrder("order2#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		});
			IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 1);
			IOrderData addedUnconfirmedOrder = unconfirmedOrders[0];
			
		runFXAction(()->{
			MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
			confirmedOrderData = opHelper.addConfirmOrder();
		});
			
			OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, confirmedOrderData);
			
			IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 1);
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
	}
	
	private IOrderData removedOrderData;
	
	@Test
	void removeUnconfirmedOrderTest() {
		runFXAction(()->{
			model.addUnconfirmedOrder("order2#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		});
			IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 1);
			IOrderData addedUnconfirmedOrder = unconfirmedOrders[0];
			
		runFXAction(()->{
			MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
			removedOrderData = opHelper.removeUnconfirmedOrder();
		});
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			
			IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			
			OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, removedOrderData);
	}
	
	private IOrderData confirmedOrderData;
	private IOrderData removedConfirmedOrderData;
	
	@Test
	void removeConfirmedOrderTest() {
		runFXAction(()->{
			model.addUnconfirmedOrder("order2#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		});
		
			IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 1);
			IOrderData addedUnconfirmedOrder = unconfirmedOrders[0];
			
			IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
		runFXAction(()->{
			MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
			confirmedOrderData = opHelper.addConfirmOrder();
		});
		OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, confirmedOrderData);
			
		confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 1);
			
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		runFXAction(()->{
			MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
			removedConfirmedOrderData = opHelper.removeConfirmedOrder();
		});
		
		runFXAction(()->{
			IOrderData[] co = model.getAllConfirmedOrders();
			Assertions.assertEquals(co.length, 0);
			
			IOrderData[] uo = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(uo.length, 0);
			
			OrderTestUtilityClass.assertOrderDatasEqual(removedConfirmedOrderData, confirmedOrderData);
		});
	}
	
	private IOrderData[] unconfirmedOrderDatas;
	private IOrderData[] confirmedOrderDatas;
	@Test
	void confirmAllOrdersTest() {
		runFXAction(()->{
			model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
			model.addUnconfirmedOrder("order6#20200813000000183#1#1:item3,5;item3,4;");
			model.addUnconfirmedOrder("order7#20200909112233937#0#0:item1,2;item2,5;");
		});
		
			IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 3);
			
			IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
			
			MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
			unconfirmedOrderDatas = opHelper.getUnconfirmedOrders().toArray(IOrderData[]::new);
			
		runFXAction(()->{
			confirmedOrderDatas = opHelper.confirmAllOrders().toArray(IOrderData[]::new);
		});
			
			confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 3);
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			
			for (IOrderData cod : confirmedOrderDatas) {
//				boolean contains = false;
//				for (IOrderData uod : unconfirmedOrderDatas) {
//					contains = contains || uod.equals(cod);
//				}
//				Assertions.assertTrue(contains);
				GeneralTestUtilityClass.arrayContains(unconfirmedOrderDatas, cod);
			}
	}
	
	@Test
	void confirmAllOrdersWithButtonTest() {
		runFXAction(()->{
			model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
			model.addUnconfirmedOrder("order6#20200813000000183#1#1:item3,5;item3,4;");
			model.addUnconfirmedOrder("order7#20200909112233937#0#0:item1,2;item2,5;");
		});
			
			IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 3);
			
			IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
		runFXAction(()->{
			MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
			unconfirmedOrderDatas = opHelper.getUnconfirmedOrders().toArray(IOrderData[]::new);
		});
		runFXAction(()->{
			MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
			opHelper.confirmAllOrdersWithoutReturn();
		});
			confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 3);
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			GeneralTestUtilityClass.performWait(waitTime);
			for (IOrderData cod : confirmedOrders) {
//				boolean contains = false;
//				for (IOrderData uod : unconfirmedOrderDatas) {
//					contains = contains || cod.equals(uod);
//				}
//				Assertions.assertTrue(contains);
				GeneralTestUtilityClass.arrayContains(unconfirmedOrderDatas, cod);
			}
	}
	
	@Test
	void confirmAllOrdersWithAutoTest() {		
		runFXAction(()->{
			MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
			opHelper.toggleOnAutoConfirm();
		});
		runFXAction(()->{
			IOrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			
			IOrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
			
			model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
			model.addUnconfirmedOrder("order6#20200813000000183#1#1:item3,5;item3,4;");
			model.addUnconfirmedOrder("order7#20200909112233937#0#0:item1,2;item2,5;");
		});
		runFXAction(()->{
			IOrderData[] co = model.getAllConfirmedOrders();
			Assertions.assertEquals(co.length, 3);
			
			IOrderData[] uo = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(uo.length, 0);
		});
	}
}
