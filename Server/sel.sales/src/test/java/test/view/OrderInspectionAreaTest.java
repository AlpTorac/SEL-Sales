package test.view;

import java.io.File;
import java.math.BigDecimal;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.application.Platform;
import javafx.stage.Stage;
import model.order.OrderData;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.StandardServerView;
import test.FXTestTemplate;
import test.GeneralTestUtilityClass;
import test.StandardServerViewOperationsUtilityClass;
import test.model.order.OrderTestUtilityClass;
import view.IView;
import view.repository.uifx.FXUIComponentFactory;

//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderInspectionAreaTest extends FXTestTemplate {
	private IServerModel model;
	private IServerController controller;
	private StandardServerView view;
	
	@BeforeEach
	void cleanUp() {
		runFXAction(()->{
			model = this.initServerModel();
			controller = this.initServerController(model);
			view = this.initServerView(model, controller);
			view.startUp();
			view.show();
			this.addDishMenuToServerModel(model);
			this.initOrders(model);
		});
	}
	
	@AfterEach
	void prep() {
		runFXAction(()->{
			model.removeAllOrders();
			view.hide();
			this.closeModel(model);
		});
	}
	
	@Test
	void confirmOrderTest() {
		runFXAction(()->{
			model.addOrder(oData2);
		});
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 1);
			OrderData addedUnconfirmedOrder = unconfirmedOrders[0];
			
		runFXAction(()->{
			this.setServerOpHelper(model, controller, view);
			confirmedOrderData = serverOpHelper.addConfirmOrder();
		});
			
			OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, confirmedOrderData);
			
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 1);
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
	}
	
	private OrderData removedOrderData;
	
	@Test
	void removeUnconfirmedOrderTest() {
		runFXAction(()->{
			model.addOrder(oData2);
		});
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 1);
			OrderData addedUnconfirmedOrder = unconfirmedOrders[0];
			
		runFXAction(()->{
			this.setServerOpHelper(model, controller, view);
			removedOrderData = serverOpHelper.removeUnconfirmedOrder();
		});
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			
			OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, removedOrderData);
	}
	
	private OrderData confirmedOrderData;
	private OrderData removedConfirmedOrderData;
	
	@Test
	void removeConfirmedOrderTest() {
		runFXAction(()->{
			model.addOrder(oData2);
		});
		
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 1);
			OrderData addedUnconfirmedOrder = unconfirmedOrders[0];
			
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
		runFXAction(()->{
			this.setServerOpHelper(model, controller, view);
			confirmedOrderData = serverOpHelper.addConfirmOrder();
		});
		OrderTestUtilityClass.assertOrderDatasEqual(addedUnconfirmedOrder, confirmedOrderData);
			
		confirmedOrders = model.getAllConfirmedOrders();
		Assertions.assertEquals(confirmedOrders.length, 1);
			
		unconfirmedOrders = model.getAllUnconfirmedOrders();
		Assertions.assertEquals(unconfirmedOrders.length, 0);
		runFXAction(()->{
			this.setServerOpHelper(model, controller, view);
			removedConfirmedOrderData = serverOpHelper.removeConfirmedOrder();
		});
		
		runFXAction(()->{
			OrderData[] co = model.getAllConfirmedOrders();
			Assertions.assertEquals(co.length, 0);
			
			OrderData[] uo = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(uo.length, 0);
			
			OrderTestUtilityClass.assertOrderDatasEqual(removedConfirmedOrderData, confirmedOrderData);
		});
	}
	
	private OrderData[] unconfirmedOrderDatas;
	private OrderData[] confirmedOrderDatas;
	@Test
	void confirmAllOrdersTest() {
		runFXAction(()->{
			this.addOrdersToModel(model);
		});
		
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 3);
			
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
			
			this.setServerOpHelper(model, controller, view);
			unconfirmedOrderDatas = serverOpHelper.getUnconfirmedOrders().toArray(OrderData[]::new);
			
		runFXAction(()->{
			confirmedOrderDatas = serverOpHelper.confirmAllOrders().toArray(OrderData[]::new);
		});
			
			confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 3);
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			
			for (OrderData cod : confirmedOrderDatas) {
//				boolean contains = false;
//				for (OrderData uod : unconfirmedOrderDatas) {
//					contains = contains || uod.equals(cod);
//				}
//				Assertions.assertTrue(contains);
				GeneralTestUtilityClass.arrayContains(unconfirmedOrderDatas, cod);
			}
	}
	
	@Test
	void confirmAllOrdersWithButtonTest() {
		runFXAction(()->{
			this.addOrdersToModel(model);
		});
			
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 3);
			
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
		runFXAction(()->{
			this.setServerOpHelper(model, controller, view);
			unconfirmedOrderDatas = serverOpHelper.getUnconfirmedOrders().toArray(OrderData[]::new);
		});
		runFXAction(()->{
			this.setServerOpHelper(model, controller, view);
			serverOpHelper.confirmAllOrdersWithoutReturn();
		});
			confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 3);
			
			unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			GeneralTestUtilityClass.performWait(waitTime);
			for (OrderData cod : confirmedOrders) {
//				boolean contains = false;
//				for (OrderData uod : unconfirmedOrderDatas) {
//					contains = contains || cod.equals(uod);
//				}
//				Assertions.assertTrue(contains);
				GeneralTestUtilityClass.arrayContains(unconfirmedOrderDatas, cod);
			}
	}
	
	@Test
	void confirmAllOrdersWithAutoTest() {		
		runFXAction(()->{
			this.setServerOpHelper(model, controller, view);
			serverOpHelper.toggleOnAutoConfirm();
		});
		runFXAction(()->{
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 0);
			
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 0);
			
			this.addOrdersToModel(model);
		});
		runFXAction(()->{
			OrderData[] co = model.getAllConfirmedOrders();
			Assertions.assertEquals(co.length, 3);
			
			OrderData[] uo = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(uo.length, 0);
		});
	}
}
