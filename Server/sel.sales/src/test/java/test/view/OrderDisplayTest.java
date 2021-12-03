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
import test.StandardServerViewOperationsUtilityClass;
import view.IView;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;
//@Execution(value = ExecutionMode.SAME_THREAD)
class OrderDisplayTest extends ApplicationTest {
	private static IServerModel model;
	private static IServerController controller;
	private static IView view;
	
	private StandardServerViewOperationsUtilityClass opHelper;
	
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
	
	private String testFolderAddress = "src"+File.separator+"test"+File.separator+"resources";
	
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
	
	@Override
	public void start(Stage stage) {
		
	}
	
	@BeforeEach
	void prep() {
		runFXAction(() -> {
			model = new ServerModel(this.testFolderAddress);
			controller = new StandardServerController(model);
			view = new StandardServerView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
			view.startUp();
			view.show();
			model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
			model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
			model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
			
			model.addUnconfirmedOrder("order2#20200809235959890#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
			model.addUnconfirmedOrder("order6#20200813000000183#1#1:item3,5;item3,4;");
			model.addUnconfirmedOrder("order7#20200909112233937#0#0:item1,2;item2,5;");
			
			opHelper = new StandardServerViewOperationsUtilityClass((StandardServerView) view, controller, model);
		});
	}
	
	@AfterEach
	void cleanUp() {
		runFXAction(() -> {
			view.hide();
			model.close();
		});
	}
	
	@Test
	void unconfirmedOrderShowingTest() {
		runFXAction(() -> {
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 3);
			
//			opHelper.clickOnUnconfirmedOrder(0);
			opHelper.assertShownOrderEquals(unconfirmedOrders[0]);
//			opHelper.clickOnUnconfirmedOrder(1);
			opHelper.assertShownOrderEquals(unconfirmedOrders[1]);
//			opHelper.clickOnUnconfirmedOrder(2);
			opHelper.assertShownOrderEquals(unconfirmedOrders[2]);
		});
	}
	
	@Test
	void multiClickOrderShowingTest() {
		runFXAction(() -> {
			OrderData[] unconfirmedOrders = model.getAllUnconfirmedOrders();
			Assertions.assertEquals(unconfirmedOrders.length, 3);
			
			opHelper.clickOnUnconfirmedOrder(0);
			opHelper.assertShownOrderEquals(unconfirmedOrders[0]);
			opHelper.clickOnUnconfirmedOrder(1);
			opHelper.assertShownOrderEquals(unconfirmedOrders[1]);
			opHelper.clickOnUnconfirmedOrder(2);
			opHelper.assertShownOrderEquals(unconfirmedOrders[2]);
		});
		runFXAction(() -> {
			model.confirmAllOrders();
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 3);
			
			opHelper.clickOnConfirmedOrder(0);
			opHelper.assertShownOrderEquals(confirmedOrders[0]);
			opHelper.clickOnConfirmedOrder(1);
			opHelper.assertShownOrderEquals(confirmedOrders[1]);
			opHelper.clickOnConfirmedOrder(2);
			opHelper.assertShownOrderEquals(confirmedOrders[2]);
		});
	}
	
	@Test
	void confirmedOrderShowingTest() {
		runFXAction(() -> {
			opHelper.confirmAllOrdersWithoutReturn();
			
			OrderData[] confirmedOrders = model.getAllConfirmedOrders();
			Assertions.assertEquals(confirmedOrders.length, 3);
			
//			opHelper.clickOnConfirmedOrder(0);
			opHelper.assertShownOrderEquals(confirmedOrders[0]);
//			opHelper.clickOnConfirmedOrder(1);
			opHelper.assertShownOrderEquals(confirmedOrders[1]);
//			opHelper.clickOnConfirmedOrder(2);
			opHelper.assertShownOrderEquals(confirmedOrders[2]);
		});
	}
}
