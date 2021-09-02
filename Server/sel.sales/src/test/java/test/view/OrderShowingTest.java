package test.view;

import static org.junit.jupiter.api.Assertions.*;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.testfx.framework.junit5.ApplicationTest;

import controller.IController;
import controller.MainController;
import javafx.application.Application;
import javafx.stage.Stage;
import model.IModel;
import model.Model;
import model.dish.IDishMenuItemDataFactory;
import model.dish.IDishMenuItemIDFactory;
import model.order.IOrderData;
import test.GeneralTestUtilityClass;
import test.MainViewOperationsUtilityClass;
import test.model.order.ClientSimulant;
import view.IView;
import view.MainView;
import view.composites.OrderTrackingArea;
import view.repository.IListView;
import view.repository.uifx.FXUIComponentFactory;

class OrderShowingTest extends ApplicationTest {
	private static IModel model;
	private static IDishMenuItemDataFactory menuItemDataFac;
	private static IDishMenuItemIDFactory menuItemIDFac;
	
	private static IController controller;
	private static IView view;
	
	private static ExecutorService pool = Executors.newFixedThreadPool(10);
	
	@BeforeEach
	public void prep() {
		model.removeAllUnconfirmedOrders();
	}
	
	@AfterEach
	public void cleanUp() {
		model.removeAllUnconfirmedOrders();
	}
	
	@Override
	public void start(Stage stage) {
		model = new Model();
		controller = new MainController(model);
		view = new MainView(new FXUIComponentFactory(), controller, model);
		view.startUp();
		menuItemDataFac = model.getItemDataCommunicationProtocoll();
		menuItemIDFac = model.getItemIDCommunicationProtocoll();
		model.addMenuItem(menuItemDataFac.constructData(
				"aaa",
				BigDecimal.valueOf(2.34),
				BigDecimal.valueOf(5),
				BigDecimal.valueOf(4),
				"item1", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"bbb",
				BigDecimal.valueOf(5.67),
				BigDecimal.valueOf(1),
				BigDecimal.valueOf(0.5),
				"item2", menuItemIDFac));
		
		model.addMenuItem(menuItemDataFac.constructData(
				"ccc",
				BigDecimal.valueOf(3.34),
				BigDecimal.valueOf(4),
				BigDecimal.valueOf(3.5),
				"item3", menuItemIDFac));
	}
	@Test
	void duplicateOrderTest() {
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		model.addUnconfirmedOrder("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1");
		
		Assertions.assertEquals(model.getAllUnconfirmedOrders().length, 1);
		
		MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
		
		Assertions.assertEquals(opHelper.getUnconfirmedOrders().size(), 1);
	}

	@Test
	void duplicateAsynchroneOrderTest() {
		ClientSimulant c1 = new ClientSimulant("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c2 = new ClientSimulant("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c3 = new ClientSimulant("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c4 = new ClientSimulant("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c5 = new ClientSimulant("order2-20200809235959299-1-0:item1,2;item2,3;item3,5;item1,7;item2,0;item3,1", model);
		ClientSimulant c6 = new ClientSimulant("order6-20200813000000183-1-1:item3,5;item3,4;", model);
		ClientSimulant c7 = new ClientSimulant("order7-20200909112233937-0-0:item1,2;item2,5;", model);
		ClientSimulant c8 = new ClientSimulant("order6-20200813000000183-1-1:item3,5;item3,4;", model);
		ClientSimulant c9 = new ClientSimulant("order9-20210709000000745-1-1:item3,5;", model);
		ClientSimulant c10 = new ClientSimulant("order6-20200813000000183-1-1:item3,5;item3,4;", model);
		
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
		
		MainViewOperationsUtilityClass opHelper = new MainViewOperationsUtilityClass((MainView) view, controller, model);
		
		Assertions.assertEquals(opHelper.getUnconfirmedOrders().size(), 4);
	}
	
}
