package test.view;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.parallel.Execution;
import org.junit.jupiter.api.parallel.ExecutionMode;
import org.testfx.framework.junit5.ApplicationTest;

import javafx.stage.Stage;
import server.controller.IServerController;
import server.controller.StandardServerController;
import server.model.IServerModel;
import server.model.ServerModel;
import server.view.StandardServerView;
import test.GeneralTestUtilityClass;
import test.MainViewOperationsUtilityClass;
import test.model.order.ClientSimulant;
import view.IView;
import view.repository.uifx.FXAdvancedUIComponentFactory;
import view.repository.uifx.FXUIComponentFactory;

@Execution(value = ExecutionMode.SAME_THREAD)
class UnconfirmedOrderTest extends ApplicationTest {
	private static IServerModel model;
	
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
	
	private static IServerController controller;
	private static IView view;
	
	private static ExecutorService pool = Executors.newCachedThreadPool();
	
	@BeforeEach
	public void prep() {
		model.removeAllUnconfirmedOrders();
	}
	
	@AfterEach
	public void cleanUp() {
		model.removeAllUnconfirmedOrders();
		model.close();
	}
	
	@Override
	public void start(Stage stage) {
		model = new ServerModel();
		controller = new StandardServerController(model);
		view = new StandardServerView(new FXUIComponentFactory(), new FXAdvancedUIComponentFactory(), controller, model);
		view.startUp();
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i1Name, i1id, i1PorSize, i1ProCost, i1Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i2Name, i2id, i2PorSize, i2ProCost, i2Price));
		model.addMenuItem(model.getDishMenuHelper().serialiseMenuItemForApp(i3Name, i3id, i3PorSize, i3ProCost, i3Price));
	}
	@Test
	void duplicateOrderTest() {
		model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		
		Assertions.assertEquals(model.getAllUnconfirmedOrders().length, 1);
		
		MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((StandardServerView) view, controller, model);
		
		Assertions.assertEquals(opHelper.getUnconfirmedOrders().size(), 1);
	}

	@Test
	void duplicateAsynchroneOrderTest() {
		ClientSimulant c1 = new ClientSimulant("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c2 = new ClientSimulant("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c3 = new ClientSimulant("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c4 = new ClientSimulant("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c5 = new ClientSimulant("order2#20200809235959299#1#0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c6 = new ClientSimulant("order6#20200813000000183#1#1:item3,5;item3,4;", model);
		ClientSimulant c7 = new ClientSimulant("order7#20200909112233937#0#0:item1,2;item2,5;", model);
		ClientSimulant c8 = new ClientSimulant("order6#20200813000000183#1#1:item3,5;item3,4;", model);
		ClientSimulant c9 = new ClientSimulant("order9#20210709000000745#1#1:item3,5;", model);
		ClientSimulant c10 = new ClientSimulant("order6#20200813000000183#1#1:item3,5;item3,4;", model);
		
		Collection<ClientSimulant> cs = new ArrayList<ClientSimulant>();
		cs.add(c1);
		cs.add(c2);
		cs.add(c3);
		cs.add(c4);
		cs.add(c5);
		cs.add(c6);
		cs.add(c7);
		cs.add(c8);
		cs.add(c9);
		cs.add(c10);
		
		@SuppressWarnings("rawtypes")
		Collection<Future> fs = new ArrayList<Future>();
		
		for (ClientSimulant c : cs) {
			fs.add(pool.submit(c));
		}
		
		while (!fs.stream().allMatch(f -> f.isDone())) {
			
		}
		
		pool.shutdown();
		
		Assertions.assertEquals(model.getAllUnconfirmedOrders().length, 4);
		
		MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((StandardServerView) view, controller, model);
		
		GeneralTestUtilityClass.performWait(300);
		
		Assertions.assertEquals(opHelper.getUnconfirmedOrders().size(), 4);
	}
	
}
